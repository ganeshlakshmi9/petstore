package test.system.com.pyxis.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PetStoreDriver;
import test.support.com.pyxis.petstore.web.SystemTest;
import test.support.com.pyxis.petstore.web.server.ServerDriver;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTest.systemTesting;

public class SearchFeature {

    SystemTest context = systemTesting();

    ServerDriver server = context.startServer();
    WebDriver browser = context.startBrowser();
    PetStoreDriver petstore = new PetStoreDriver(browser);

    @Before public void
    startApplication() throws Exception {
        petstore.open(context.routing());
    }

    @After public void
    stopApplication() throws Exception {
        petstore.close();
        context.stopServer(server);
        context.stopBrowser(browser);
        context.cleanUp();
    }

    @Test public void
    searchesForAProductNotAvailableInStore() throws Exception {
        context.given(aProduct().withName("Labrador Retriever"));

        petstore.searchFor("Dalmatian");
        petstore.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() throws Exception {
        context.given(aProduct().withNumber("LAB-1234").withName("Labrador Retriever"),
                aProduct().withNumber("CHE-5678").withName("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().withName("Dalmatian"));

        petstore.searchFor("retriever");
        petstore.displaysNumberOfResults(2);
        petstore.displaysProduct("LAB-1234", "Labrador Retriever");
        petstore.displaysProduct("CHE-5678", "Chesapeake");
    }

}
