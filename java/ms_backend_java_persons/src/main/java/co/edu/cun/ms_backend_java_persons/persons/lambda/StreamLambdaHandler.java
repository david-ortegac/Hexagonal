package co.edu.cun.ms_backend_java_persons.persons.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import co.edu.cun.ms_backend_java_persons.persons.PersonsApplication;

public class StreamLambdaHandler implements RequestStreamHandler {

  private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

  static {
    try {
      handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(PersonsApplication.class);
      final String stageName = System.getenv("API_STAGE_NAME");
      if (stageName != null && !stageName.isBlank()) {
        handler.stripBasePath("/" + stageName);
      }
    } catch (ContainerInitializationException exception) {
      throw new RuntimeException("No se pudo inicializar Spring Boot para AWS Lambda", exception);
    }
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    handler.proxyStream(inputStream, outputStream, context);
  }
}
