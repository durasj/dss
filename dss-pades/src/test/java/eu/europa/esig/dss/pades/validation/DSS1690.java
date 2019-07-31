package eu.europa.esig.dss.pades.validation;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.client.http.IgnoreDataLoader;
import eu.europa.esig.dss.jaxb.diagnostic.XmlTimestampedObject;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.TimestampedObjectType;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.wrapper.DiagnosticData;
import eu.europa.esig.dss.validation.reports.wrapper.TimestampWrapper;

@RunWith(Parameterized.class)
public class DSS1690 {

	@Parameterized.Parameters
	public static Object[][] data() {
		return new Object[100][0];
	}

	public DSS1690() {
	}

	@Test
	public void validateArchiveTimestampsOrder() {

		String firstTimestampId = "950D06E9BC8B0CDB73D88349F14D3BC702BF4947752A121A940EE03639C1249D";

		DSSDocument dssDocument = new InMemoryDocument(getClass().getResourceAsStream("/validation/Test.signed_Certipost-2048-SHA512.extended-LTA.pdf"));

		PDFDocumentValidator validator = new PDFDocumentValidator(dssDocument);
		validator.setCertificateVerifier(getOfflineCertificateVerifier());
		Reports reports = validator.validateDocument();

		DiagnosticData diagnosticData = reports.getDiagnosticData();
		TimestampWrapper firstATST = diagnosticData.getTimestampById(firstTimestampId);
		List<XmlTimestampedObject> timestampedObjects = firstATST.getTimestampedObjects();
		for (XmlTimestampedObject xmlTimestampedObject : timestampedObjects) {
			if (TimestampedObjectType.TIMESTAMP.equals(xmlTimestampedObject.getCategory())) {
				fail("First timestamp can't cover the second one");
			}
		}

	}

	protected CertificateVerifier getOfflineCertificateVerifier() {
		CertificateVerifier cv = new CommonCertificateVerifier();
		cv.setDataLoader(new IgnoreDataLoader());
		return cv;
	}
}
