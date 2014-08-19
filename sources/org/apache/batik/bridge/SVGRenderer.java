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

public class SVGRenderer {

    /** The resulting PDF. */
    public  String output = "/var/sfsite/jobs/3-ecozl9lt4a/result-itext.pdf";
    /** The Input svg */
    public  String input = "/var/sfsite/jobs/3-ecozl9lt4a/result.svg";

    public  float width = 1018.8f;
    public  float height = 788.4f;
    
    /** The SVG document factory. */
    protected SAXSVGDocumentFactory factory;
    /** The SVG bridge context. */
    protected BridgeContext ctx;
    /** The GVT builder */
    protected GVTBuilder builder;
    
    /** Creates an SvgToPdf object. */
    public SVGRenderer(String inputFile, String outputFile, float width, float height) {
    	this.input=inputFile;
    	this.output=outputFile;
    	this.width=width;
    	this.height=height;
    	
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
        
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);

        builder = new GVTBuilder();
    }
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf() throws IOException, DocumentException {
    	Document document = new Document(new Rectangle(width, height));
    	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(output));
    	document.open();
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
        PdfTemplate pdfTemplate = pdfContentByte.createTemplate(width, height);
        
        Graphics2D g2d = new PdfGraphics2DExt(pdfWriter, pdfContentByte, width, height);
        SVGDocument svgDoc = factory.createSVGDocument(input);
        GraphicsNode mapGraphics = builder.build(ctx, svgDoc);
        mapGraphics.paint(g2d);
        g2d.dispose();
        
        pdfContentByte.addTemplate(pdfTemplate, 0, 0);
        document.close();
    }
    
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
    	renderUsingIText( new File("/var/sfsite/jobs/3-8jx93oun/result.svg").toURL().toString(), "/var/sfsite/jobs/3-8jx93oun", "simple-itextfixed", "pdf");    }
    
	public static void renderUsingIText(String svgUrl, String destFolder, String destFile, String fileType)
			throws IOException {

		try {

			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			SVGDocument svgDoc = factory.createSVGDocument(svgUrl);
			float width=
					Float.parseFloat(svgDoc.getDocumentElement()
							.getAttribute("width").replaceAll("px", ""));
			float height = 
					Float.parseFloat(svgDoc.getDocumentElement()
							.getAttribute("height").replaceAll("px", ""));
			new SVGRenderer(svgUrl, new File(destFolder, destFile + "." + fileType).getAbsolutePath(), width, height).createPdf();
			
		} catch (Exception e) {
			throw new IOException(e);
		} finally{
		}
	}
}