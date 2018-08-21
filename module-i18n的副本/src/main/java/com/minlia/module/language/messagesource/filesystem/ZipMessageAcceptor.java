/**
 *
 */
package com.minlia.module.language.messagesource.filesystem;

import com.minlia.module.language.messagesource.util.LocaleUtils;
import com.minlia.module.language.messagesource.MessageAcceptor;
import com.minlia.module.language.messagesource.Messages;

import java.io.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * {@link MessageAcceptor} implementation that is able to write messages in a zip-compressed file or stream.
 *
 */
public class ZipMessageAcceptor implements MessageAcceptor {

    private ZipOutputStream zip;


    public ZipMessageAcceptor(OutputStream stream) {

        this.zip = new ZipOutputStream(stream);

    }


    public ZipMessageAcceptor(File file) throws FileNotFoundException {

        this.zip = new ZipOutputStream(new FileOutputStream(file));

    }


    public void appendToZip(String filename, ByteArrayOutputStream bos) throws IOException {

        zip.putNextEntry(new ZipEntry(filename));
        zip.write(bos.toByteArray());
        // and close the entry
        zip.closeEntry();

    }


    /*
     * (non-Javadoc)
     *
     * @see org.synyx.messagesource.MessageAcceptor#setMessages(java.lang.String, org.synyx.messagesource.Messages)
     */
    public void setMessages(String basename, Messages messages) {

        Set<Locale> locales = messages.getLocales();
        for (Locale locale : locales) {
            String fileName = getFileNameFor(basename, locale);

            Map<String, String> msgs = messages.getMessages(locale);
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : msgs.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {

                properties.store(bos, "messages for basename " + basename + " and locale " + locale);
                appendToZip(fileName, bos);
            } catch (IOException e) {
                throw new RuntimeException("Could not write messages for basename " + basename + " and locale "
                        + locale + "to outputstream: " + e.getMessage(), e);
            }

        }

    }


    /**
     *
     */
    public void initialize() {

        zip.setLevel(Deflater.DEFAULT_COMPRESSION);

    }


    public void finish() {

        try {
            zip.close();
        } catch (IOException e) {

        }

    }


    /**
     * @param locale
     */
    private String getFileNameFor(String basename, Locale locale) {

        String localePart = LocaleUtils.fromLocale(locale);
        if (localePart == null) {
            localePart = "";
        } else {
            localePart = "_" + localePart;
        }
        String fileName = String.format("%s%s.properties", basename, localePart);

        return fileName;

    }

}
