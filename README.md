# DataSynthesizer
[![Java CI with Maven](https://github.com/phrocker/datasynthesizer/actions/workflows/maven.yml/badge.svg)](https://github.com/phrocker/datasynthesizer/actions/workflows/maven.yml)
## Overview

DataSyntheiszer is a tool that synthesizes data based on a given schema. The tool is designed to be used in the following scenarios:
    
    * Generating synthetic data for testing data pipelines
    * Generating synthetic data for testing data quality
    * Generating synthetic data for query testing
    * Generating synthetic data for training machine learning models
    * Generating synthetic data for data privacy

## Examples

### OpenAI GPT-3.5 Api Key Provider

```java
    // you can use the following to load the API key from the environment variable OPENAI_API_KEY
    // The builder supports loading the API key from the env or a string. 
    TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();
```

### OpenAI GPT-3.5 Query Generation

Generate a query based on a given schema and query type. You must define the data dictionary programmatically
```java
    TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();
    GenerativeAPI chatGPT = new GenerativeAPI(provider);
        List<QueryConfiguration.DataDictionaryDefinition> dataDictionary = new ArrayList<>();
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder()
                .fieldName("carType")
                .type(FieldType.EXACT)
                .build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder()
                .fieldName("carColor")
                .type(FieldType.EXACT)
                .build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder()
                .fieldName("carModel")
                .type(FieldType.FUZZY)
                .build());
        final QueryConfiguration queryConfig = QueryConfiguration.builder()
                .count(2) // number of queries to generate
                // supports SQL, JEXL, and LUCENE
                .queryType(QueryType.SQL).dataDictionary(dataDictionary)
                .build();
        QueryGenerator generator = new QueryGenerator(provider, chatGPT, null, queryConfig);
        System.out.println(generator.generate());
        // [SELECT * FROM cars WHERE carType = 'sedan' AND carColor = 'red' AND carModel LIKE '%Civic%';, SELECT * FROM cars WHERE carType = 'SUV' AND carColor = 'black' AND carModel LIKE '%Explorer%';]
    }
```

### OpenAI GPT-3.5 Short Text Generation

Generate a short text. This will be a short random paragraph. 

```java
    TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();
    GenerativeAPI chatGPT = new GenerativeAPI(provider);
    ShortTextGenerator generator = new ShortTextGenerator(provider, chatGPT, null);
    System.out.println(generator.generate());
```

### OpenAI GPT-3.5 AMA

You can ask the endpoint anything through the input. 

```java
    TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();
    GenerativeAPI chatGPT = new GenerativeAPI(provider);
    ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();
    Response hello = chatGPT.sample(request, Response.class);
    System.out.println(hello.concatenateResponses());
```

### Thanks and Shoutouts

This project is inspired by the following projects:

* [LogSynth](https://github.com/tdunning/log-synth)
* [ChatGPT](https://github.com/LiLittleCat/ChatGPT/)

