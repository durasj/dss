package eu.europa.esig.dss.validation.process.vpfswatsp.checks;

import eu.europa.esig.dss.jaxb.detailedreport.XmlPSV;
import eu.europa.esig.dss.jaxb.detailedreport.XmlValidationProcessArchivalData;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.IMessageTag;
import eu.europa.esig.dss.validation.process.MessageTag;
import eu.europa.esig.dss.validation.reports.wrapper.TimestampWrapper;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class PastTimestampValidation extends ChainItem<XmlValidationProcessArchivalData> {
	
	private XmlPSV xmlPSV;

	private Indication indication;
	private SubIndication subIndication;
	
	private static final String PSV_BLOCK_SUFFIX = "-PSV";

	public PastTimestampValidation(XmlValidationProcessArchivalData result, XmlPSV xmlPSV, 
			TimestampWrapper timestamp, LevelConstraint constraint) {
		super(result, constraint, timestamp.getId() + PSV_BLOCK_SUFFIX);
		this.xmlPSV = xmlPSV;
	}

	@Override
	protected boolean process() {
		if (!isValid(xmlPSV)) {
			indication = xmlPSV.getConclusion().getIndication();
			subIndication = xmlPSV.getConclusion().getSubIndication();
			return false;
		}
		return true;
	}

	@Override
	protected IMessageTag getMessageTag() {
		return MessageTag.PSV_IPTVC;
	}

	@Override
	protected IMessageTag getErrorMessageTag() {
		return MessageTag.PSV_IPTVC_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return indication;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return subIndication;
	}

}
