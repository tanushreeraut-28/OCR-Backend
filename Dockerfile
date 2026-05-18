FROM eclipse-temurin:17

WORKDIR /app

# Install Tesseract OCR
RUN apt-get update && apt-get install -y tesseract-ocr

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/ocr_project-0.0.1-SNAPSHOT.jar"]