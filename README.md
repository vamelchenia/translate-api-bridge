# Translate-api bridge
Backend java proxy server for Nerd Translator android app to connect it with Google Cloud APIs. 

## Table of Contents
- [Technologies](#technologies)
- [Usage](#usage)
- [Development](#development)
- [Project Team](#project-team)

## Technologies
- [Java](https://java.com/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Google cloud](https://cloud.google.com/)
- [Jackson](https://github.com/FasterXML/jackson)
- [Springdoc](https://springdoc.org/)
- [Lombok](https://projectlombok.org/)

## Usage
To utilize this backend Java Spring Boot proxy server for communication between your Android application and Google Cloud APIs, follow these steps:

1. **Download Source Code:**
   Clone or download the source code repository to your local machine.

   ```bash
   $ git clone https://github.com/vamelchenia/translate-api-bridge.git
   ```

2. **Sensitive Properties File:**
   Create a file named `sensitive.properties` in the root of the project. Add your Google Cloud credentials to this file. The credentials should be provided by Google Cloud.

   Example of `sensitive.properties`:

   ```properties
    TYPE = your_property
    PROJECT_ID = your_property
    PRIVATE_KEY_ID = your_property
    PRIVATE_KEY = your_property
    CLIENT_EMAIL = your_property
    CLIENT_ID = your_property
    AUTH_URI = your_property
    TOKEN_URI = your_property
    AUTH_PROVIDER_X509_CERT_URL = your_property
    CLIENT_X509_CERT_URL = your_property
    UNIVERSE_DOMAIN = your_property
   ```

   Replace `your_property` with the actual values provided by Google Cloud.  

3. **Build and Run:**
   Use your preferred Java build tool to build the project. For instance, with Maven:

   ```bash
   $ mvn clean install
   ```

   After a successful build, you can run the application:

   ```bash
   $ java -jar target/translate-api-bridge.jar
   ```

4. **Integration with Android App:**
    In your Android application, configure the API endpoints to communicate with the deployed backend server. The available endpoints are documented using [SpringDoc](https://springdoc.org/) for easy reference.

    #### Endpoints:

   1. **Endpoint 1 - [POST] Post text to receive it's translation:**
       - **Path:** `/translation/{originalLanguage}-{targetLanguage}`
       - **Description:** Post a text to translate with original language and target language codes as parameters and get translation with additional data as audio and part of speech if provided as response.
       - **Request:**
           - Method: POST
           - Path Variable:
               - `originalLanguage` (String) - Original language code to translate from
               - `targetLanguage` (String) - Target language code to translate to
           - Parameters:
               - `originalText` (String) - Original language text that should be translated
       - **Response:**
           - HttpStatus with Translation data in JSON format

   2. **Endpoint 2 - [GET] Get supported languages:**
       - **Path:** `/translation/languages`
       - **Description:** Get a map of all supported languages with their codes.
       - **Request:**
           - Method: GET
       - **Response:**
           - HttpStatus with supported languages data in JSON format

    Ensure that your Android application makes requests to these endpoints using the appropriate HTTP methods and includes any required parameters in the request body or as path variables. The detailed documentation provided by SpringDoc makes it easy to integrate and interact with the backend API seamlessly.
    
    You can try all endpoint out using Springdoc UI after copy and run the application:

   ```
   http://your-host:your-port/swagger-ui/index.html
   ``` 
   Users should replace ``your-host`` with their host address and ``your-port`` with their port number. For instance:

    <http://localhost:8083/swagger-ui/index.html>

    In this example, localhost is the host, and 8083 is the port. Each user can substitute these values with their own, depending on how they have configured their server.  
5. **Note:** Ensure that the backend server is running and accessible from your Android application. You may need to adjust firewall settings or deploy the backend to a publicly accessible server.

Now, your Android application should be able to communicate seamlessly with Google Cloud APIs through the Java Spring Boot proxy server.

## Development

### Requirements
To install and run the project, [Java](https://java.com/) v17+ is required.

## Project Team

- [Valeriia Amelchenia](https://github.com/vamelchenia) — Senior Software QA Engineer
- [Artem Prokhorov](https://github.com/Electron3D) — Back-End Engineer
- [Evgeny Savonkin](https://github.com/evgenysavonkin) — Back-End Engineer
