package com.welovecoding.tutorial.api.v2.resource;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV3;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.module.jsv.JsonSchemaValidator;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.jayway.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import com.jayway.restassured.response.Response;
import java.io.File;
import java.io.InputStream;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import util.IntegrationTest;

/**
 *
 * @author Michael Koppen
 */
public class VideoResourceIT extends IntegrationTest {

  @Rule
  // Get name of actual Test with test.getMethodName()
  public TestName test = new TestName();

  private static final String ANY_JSON_ARRAY_SCHEMA = "json-schema/any-json-array-schema.json";
  private static final String ANY_JSON_OBJECT_SCHEMA = "json-schema/any-json-object-schema.json";

  @BeforeClass
  public static void setUpClass() throws Exception {
    IntegrationTest.setUpClass();
    flatXmlDataSet = new FlatXmlDataSet(new File("src/test/resources", "full.xml"), false, true);
    JsonSchemaValidator.settings = settings().with().jsonSchemaFactory(
            JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV3).freeze()).freeze()).
            and().with().checkedValidation(false);
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    IntegrationTest.tearDownClass();
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  @After
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  private static InputStream getSchema(String schemaPath) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaPath);
  }

  /**
   * Test of getVideos method, of class VideoResource.
   */
  @Test
  public void testGetVideo() throws Exception {
    System.out.println("getVideo");
    InputStream schema = getSchema(ANY_JSON_OBJECT_SCHEMA);
    Response resp
            = given().
            pathParam("id", 1).
            when().
            get(ROOT + "/rest/service/v2/videos/{id}").then().
            extract().response();
    System.out.println("RESPONSE: ");
    resp.prettyPrint();

    // TODO map empty lists and null objects to [] and {}
    if (!resp.body().print().isEmpty()) {
      resp.then().assertThat().body(matchesJsonSchema(schema));
    }
  }

}
