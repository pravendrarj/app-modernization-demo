package com.legacy.inventory.service;

import com.legacy.inventory.model.Product;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTDATED PATTERNS:
 * - Uses javax.mail (should be jakarta.mail)
 * - Uses javax.xml.bind (JAXB removed from JDK 11+)
 * - Uses java.net.HttpURLConnection (should use WebClient or RestTemplate)
 * - Uses java.net.URL constructor (deprecated in Java 20)
 * - File I/O without try-with-resources properly
 * - Hardcoded configuration values
 * - No cloud-ready patterns (uses local file system)
 */
@Service
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class);

    // OUTDATED: Hardcoded SMTP configuration
    @Value("${mail.smtp.host:localhost}")
    private String smtpHost;

    @Value("${mail.smtp.port:25}")
    private int smtpPort;

    @Value("${notification.from:noreply@legacy-app.com}")
    private String fromAddress;

    // OUTDATED: Using deprecated javax.mail API
    public void sendEmailNotification(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));
            // SECURITY: No TLS/SSL configuration
            props.put("mail.smtp.auth", "false");

            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            message.setSentDate(new Date());

            Transport.send(message);
            logger.info("Email sent to: " + to);
        } catch (Exception e) {
            // OUTDATED: Catching generic Exception, printing stack trace
            logger.error("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // OUTDATED: Send low stock alerts
    public void sendLowStockAlert(List<Product> lowStockProducts) {
        StringBuilder body = new StringBuilder();
        body.append("Low Stock Alert!\n\n");

        for (Product product : lowStockProducts) {
            body.append("Product: ").append(product.getProductName())
                .append(" | SKU: ").append(product.getSku())
                .append(" | Current Stock: ").append(product.getQuantity())
                .append("\n");
        }

        sendEmailNotification("admin@legacy-app.com", "Low Stock Alert", body.toString());
    }

    // OUTDATED: Using java.net.URL and HttpURLConnection (deprecated pattern)
    @SuppressWarnings("deprecation")
    public String callExternalApi(String apiUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // OUTDATED: URL constructor deprecated in Java 20
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // SECURITY: No SSL certificate validation
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                logger.error("API call failed with response code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            logger.error("API call failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            // OUTDATED: Manual resource cleanup instead of try-with-resources
            if (reader != null) {
                try { reader.close(); } catch (IOException e) { /* ignore */ }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // OUTDATED: Writing reports to LOCAL file system (not cloud-ready)
    public void exportProductReport(List<Product> products, String filePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PrintWriter writer = null;

        try {
            // OUTDATED: Writing to local filesystem, not cloud storage
            writer = new PrintWriter(new FileWriter(filePath));
            writer.println("Product Report - Generated: " + sdf.format(new Date()));
            writer.println("================================================");

            for (Product product : products) {
                writer.println(String.format("%-20s | %-10s | $%-8.2f | Qty: %d",
                    product.getProductName(),
                    product.getSku(),
                    product.getPrice(),
                    product.getQuantity()));
            }

            writer.println("================================================");
            writer.println("Total Products: " + products.size());

            logger.info("Report exported to: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to export report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // OUTDATED: Reading file using deprecated commons-io
    public String readFileContent(String filePath) {
        try {
            File file = new File(filePath);
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            logger.error("Failed to read file: " + e.getMessage());
            return null;
        }
    }
}
