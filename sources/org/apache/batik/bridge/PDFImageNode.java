package org.apache.batik.bridge;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.AbstractGraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * A node that holds a Graphics2D image.
 */
public class PDFImageNode extends AbstractGraphicsNode {


	private BridgeContext ctx;
	private Element e;
	private ParsedURL purl;
	private String url;
	Rectangle boundaryRectangle;

    public PDFImageNode(BridgeContext ctx, Element e, ParsedURL purl) {
		this.ctx=ctx;
		this.e=e;
		this.purl=purl;
		url=purl.toString();
		PdfReader reader;
		try {
			reader = new PdfReader(url);
			boundaryRectangle = reader.getPageSize(1);
			reader.close();
			
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

    public void setImage(Filter filter) {
        fireGraphicsNodeChangeStarted();
        invalidateGeometryCache();
        //calculateimagebound
        fireGraphicsNodeChangeCompleted();
    }
    
	/** {@inheritDoc} */
    public Shape getOutline() {
        return getPrimitiveBounds();
    }

    /** {@inheritDoc} */
    public void primitivePaint(Graphics2D g2d) {
    	PdfGraphics2DExt pdfG2d = (PdfGraphics2DExt) g2d;
    	PdfReader reader;
		try {
			reader = new PdfReader(url);
	    	PdfWriter writer = pdfG2d.getPdfWriter();
	    	
	    	PdfImportedPage embeddedPdfPage = writer.getImportedPage(reader, 1);
	    	Image img = Image.getInstance(embeddedPdfPage); 
	    	
	    	img.setAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
	    	
	    	//rotate
	    	String rotationDegrees = ((Element)e.getParentNode()).getAttributeNodeNS("http://www.shutterfly.com/module/layout/v5","rotationEnum").getValue();
	    	float deg=Float.parseFloat(rotationDegrees);
	    	img.setRotationDegrees(deg);
	    	
	    	//position
	    	String x = e.getAttribute("x");
	    	String y = e.getAttribute("y");
	    	float xfloat = Float.parseFloat(x);
	    	float yfloat = Float.parseFloat(y);
            AffineTransform relativePositionTransform= new AffineTransform();
            relativePositionTransform.translate(xfloat, yfloat);
            
            //draw
            pdfG2d.drawPDfImage(img, relativePositionTransform);
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


    }

    /** {@inheritDoc} */
    public Rectangle2D getGeometryBounds() {
        return getPrimitiveBounds();
    }

    /** {@inheritDoc} */
    public Rectangle2D getPrimitiveBounds() {
    	//SFLY:: TODO
        return new Rectangle2D.Double(0, 0,
                boundaryRectangle.getWidth(),
                boundaryRectangle.getHeight());
    }

    /** {@inheritDoc} */
    public Rectangle2D getSensitiveBounds() {
        //No interactive features, just return primitive bounds
        return getPrimitiveBounds();
    }

}