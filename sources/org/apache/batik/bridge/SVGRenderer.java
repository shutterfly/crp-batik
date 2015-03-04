package org.apache.batik.bridge;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.shutterfly.crp.common.SflyColor;

/**
 * CRP: class added.
 * This class provides a method to convert from SVG file to a PDF file.
 */
public class SVGRenderer {

	/** The resulting PDF. */
	private final String output;
	/** The Input svg */
	private final String input;

	private final float width;
	private final float height;

	/** The SVG document factory. */
	protected SAXSVGDocumentFactory factory;
	/** The SVG bridge context. */
	protected BridgeContext ctx;
	/** The GVT builder */
	protected GVTBuilder builder;

	/**
	 * Creates an SvgToPdf object.
	 * 
	 * @param rgb_svgToCmyk
	 */
	public SVGRenderer(String inputFile, String outputFile, float width, float height, Map<String, SflyColor> rgb_svgToCmyk) {
		this.input = inputFile;
		this.output = outputFile;
		this.width = width;
		this.height = height;
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		factory = new SAXSVGDocumentFactory(parser);

		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		ctx = new BridgeContext(userAgent, loader, rgb_svgToCmyk);
		ctx.setDynamicState(BridgeContext.DYNAMIC);

		builder = new GVTBuilder();
	}

	/**
	 * Creates a PDF document.
	 *
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void createPdf() throws IOException, DocumentException {
		Document document = new Document(new Rectangle(width, height));
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(output));
		pdfWriter.setCompressionLevel(9);
		pdfWriter.setFullCompression();
		document.open();
		PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
		PdfTemplate pdfTemplate = pdfContentByte.createTemplate(width, height);

		// @@@CRP: Ankit. Configure PDFGraphics to compress jpegs with 95% quality
		Graphics2D g2d = new PdfGraphics2DExt(pdfWriter, pdfContentByte, width, height, null, false, false, 0.95f);
		// new PdfGraphics2DExt(pdfWriter, pdfContentByte, width, height);

		SVGDocument svgDoc = factory.createSVGDocument(input);
		GraphicsNode mapGraphics = builder.build(ctx, svgDoc);
		mapGraphics.paint(g2d);
		g2d.dispose();

		pdfContentByte.addTemplate(pdfTemplate, 0, 0);
		document.close();
	}

	public static void renderUsingIText(String svgUrl, String destFolder, String destFile, String fileType, Map<String, SflyColor> rgb_svgToCmyk)
			throws IOException {

		try {

			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			SVGDocument svgDoc = factory.createSVGDocument(svgUrl);

			String strWidth = svgDoc.getDocumentElement().getAttribute("width");
			String strHeight = svgDoc.getDocumentElement().getAttribute("height");
			float width = Float.parseFloat(strWidth.replaceAll("px", "").replaceAll("in", "")) * (strWidth.endsWith("in") ? 72 : 1);
			float height = Float.parseFloat(strHeight.replaceAll("px", "").replaceAll("in", "")) * (strHeight.endsWith("in") ? 72 : 1);
			new SVGRenderer(svgUrl, new File(destFolder, destFile + "." + fileType).getAbsolutePath(), width, height, rgb_svgToCmyk).createPdf();

		} catch (Exception e) {
			throw new IOException(e);
		} finally {
		}
	}
}