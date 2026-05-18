FROM eclipse-temurin:17

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libtesseract-dev

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/OCR-Backend-0.0.1-SNAPSHOT.jar"]