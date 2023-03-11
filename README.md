# DataSynthesizer
## Overview

DataSyntheiszer is a tool that synthesizes data based on a given schema. The tool is designed to be used in the following scenarios:
    
    * Generating synthetic data for testing data pipelines
    * Generating synthetic data for testing data quality
    * Generating synthetic data for query testing
    * Generating synthetic data for training machine learning models
    * Generating synthetic data for data privacy

## Open AI Examples

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

## SchemaSynthesis Examples

### The following example unit test will generate a text message with the 
```java
    final String jsonText = IOUtils.toString(
    this.getClass().getResourceAsStream("/samplers/textMessage.json"),
    "UTF-8"
    );

    Schema schema = Schema.builder().from(jsonText).build();

    SchemaSynthesizer synthesizer = new SchemaSynthesizer(schema);
    var record = synthesizer.generateRecords(1);
    Assert.assertEquals(1, record.size());
```
This will generate the following output:
        
```json lines
{"imei":"458855761073067",
  "from_phone_number":"228-153-9629",
  "to_phone_number":"228-159-6269",
  "message":"\n\nThe sun was setting behind the mountains, casting a warm orange glow across the sky. There was a crispness to the air, as if winter was just around the corner. John leaned against the"}
```

Since schema generation uses JSON schemas, the /samplers/textMessage.json included below for convenience:

```json lines
[{"name":  "imei",
  "class": "phoneid"},
  {
  "name": "from_phone_number",
  "class": "phonenumber",
  "areaCodeMin": "228",
  "areaCodeMax": "228"
},{
  "name": "to_phone_number",
  "class": "phonenumber",
  "areaCodeMin": "228",
  "areaCodeMax": "228"
},
  {
    "name": "message",
    "class": "chatgptshorttext"
  }]
```



### Thanks and Shoutouts

This project is inspired by the following projects:

* [LogSynth](https://github.com/tdunning/log-synth)
* [ChatGPT](https://github.com/LiLittleCat/ChatGPT/)

