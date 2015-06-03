package org.apache.batik.bridge;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.AbstractGraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * CRP: added class.
 * 
 * This is to handle case where the SVG image refers to a pdf file (such as for CLM Text.)
 * (Default batik doesn't know how to deal with pdf images.) 
 * See also SVGImageElementBridge, where this is created.
 * 
 * This is a node that holds a Graphics2D image for PDF. 
 */
public class PDFImageNode extends AbstractGraphicsNode {

	private static Logger logger = Logger.getLogger(PDFImageNode.class);
	private final ParsedURL purl;
	private final String url;
	Rectangle boundaryRectangle;

	public PDFImageNode(BridgeContext ctx, Element e, ParsedURL purl) {
		this.purl = purl;
		url = purl.toString();
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
		// calculateimagebound
		fireGraphicsNodeChangeCompleted();
	}

	/** {@inheritDoc} */
	@Override
	public Shape getOutline() {
		return getPrimitiveBounds();
	}

	/** {@inheritDoc} */
	@Override
	public void primitivePaint(Graphics2D g2d) {
		if ((url != null) && url.endsWith(".pdf") && !(g2d instanceof PdfGraphics2DExt)) {
			logger.warn("graphics2D in use" + g2d.getClass().getName() + " does not support rendering pdf:" + purl);
			return;
		}
		PdfGraphics2DExt pdfG2d = (PdfGraphics2DExt) g2d;
		PdfReader reader;
		try {
			reader = new PdfReader(url);
			PdfWriter writer = pdfG2d.getPdfWriter();

			PdfImportedPage embeddedPdfPage = writer.getImportedPage(reader, 1);
			Image img = Image.getInstance(embeddedPdfPage);

			img.setAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			AffineTransform relativePositionTransform = new AffineTransform();

			// draw with identity transform.
			// This PDFImage node is child of a Image node in the GVT(batiks internal rep of SVG) tree and the parent ImageNode applies the transform
			// from svg's <image transform=(..)>

			pdfG2d.drawPDfImage(img, relativePositionTransform);

		} catch (Exception ex) {
			throw new RuntimeException("Failed to paint image PDF: " + url, ex);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Rectangle2D getGeometryBounds() {
		return getPrimitiveBounds();
	}

	/** {@inheritDoc} */
	@Override
	public Rectangle2D getPrimitiveBounds() {
		// CRP TODO: Fix this.
		return new Rectangle2D.Double(0, 0, boundaryRectangle.getWidth(), boundaryRectangle.getHeight());
	}

	/** {@inheritDoc} */
	@Override
	public Rectangle2D getSensitiveBounds() {
		// No interactive features, just return primitive bounds
		return getPrimitiveBounds();
	}

	public void invalidateGeometryCachePublic() {
		invalidateGeometryCache();
	}

}