package com.ai.aiapp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {
    @Value("classpath:/csv/jobs_companies.csv")
    private Resource csvFile;

    @Value("classpath:/pdf/10089434.pdf")
    private Resource pdfFile;

    @Value("companies.json")
    private String vectorStoreName;

    @Value("resume.json")
    private String resumeStoreName;

    private final EmbeddingModel embeddingModel;

    @Autowired
    public DataLoader(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    //@Bean
    public SimpleVectorStore simpleVectorStoreCSV(EmbeddingModel embeddingModel) throws IOException, CsvException {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        String path = Path.of("src","main","resources","vectorstore", vectorStoreName).toString();
        File fileStore = new File(path);
        if(fileStore.exists()) {
            simpleVectorStore.load(fileStore);
        } else {
            CSVReader csvReader = new CSVReader(new InputStreamReader(csvFile.getInputStream()));
            List<String[]> csvData = csvReader.readAll();
            List<Document> documents = new ArrayList<>();
            for (String[] row : csvData) {
                Document document = new Document(row[0]);
                documents.add(document);
            }
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);
            simpleVectorStore.add(chunks);
            simpleVectorStore.save(fileStore);
        }
        return simpleVectorStore;
    }

    @Bean
    public SimpleVectorStore simpleVectorStore() {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        String path = Path.of("src","main","resources","vectorstore", resumeStoreName).toString();
        File fileStore = new File(path);
        if(fileStore.exists()) {
            simpleVectorStore.load(fileStore);
        } else {
            PagePdfDocumentReader documentReader = new PagePdfDocumentReader(pdfFile);
            List<Document> documents = documentReader.read();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);
            simpleVectorStore.add(chunks);
            simpleVectorStore.save(fileStore);
        }
        return simpleVectorStore;
    }
}