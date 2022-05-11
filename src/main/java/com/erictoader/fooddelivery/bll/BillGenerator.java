package com.erictoader.fooddelivery.bll;

import java.io.FileOutputStream;

import com.erictoader.fooddelivery.model.MenuItem;
import com.erictoader.fooddelivery.model.Order;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 *   This class is for generating and storing a bill containing an order's information in a PDF format
 *   @author Toader Eric-Stefan
 */
public final class BillGenerator {
    /*
     *   Constructor for creating a bill object and storing a file locally
     *   @param c The client that made the order
     *   @param p The product ordered
     *   @param o The actual order with its associated Client-Product information
     */
    public static void generateBill(Order o, ArrayList<MenuItem> contents) {
        Document doc = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("Bill" + o.getOrderID() + ".pdf"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");
            LocalDateTime orderTime = LocalDateTime.now();
            doc.open();

            doc.add(new Paragraph("Date and Time: " + dtf.format(orderTime)));
            doc.add(new Paragraph("Order ID: " + o.getOrderID()));
            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Name: " + Constants.USER_FULLNAME));
            doc.add(new Paragraph(" "));

            Integer total = 0;
            for(MenuItem mi : contents) {
                doc.add(new Paragraph("Product name: " + mi.getTitle()));
                doc.add(new Paragraph("Product price: " + mi.computePrice()));
                doc.add(new Paragraph(" "));
                total += mi.computePrice();
            }
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("TOTAL: " + total));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Eric Toader - Food Delivery Management System"));
            doc.close();
            writer.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
