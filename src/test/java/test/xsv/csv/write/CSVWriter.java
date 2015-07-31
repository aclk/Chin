package test.xsv.csv.write;

/*
 *  Copyright (C) 2010 takaji
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package dakside.csv;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import test.xsv.csv.CSVException;
import test.xsv.csv.File;
import test.xsv.csv.Format;
import test.xsv.csv.Line;

/**
 * CSV file writer
 * 
 * @author takaji
 * 
 */
public final class CSVWriter {

    protected Format format;
    protected final BufferedWriter writer;
    int row = 1;

    public CSVWriter(Writer writer) {
        this.writer = new BufferedWriter(writer);
        this.format = new Format();
    }

    public CSVWriter(Writer writer, Format format)
            throws FileNotFoundException {
        this.writer = new BufferedWriter(writer);
        this.format = format;
    }

    public CSVWriter(BufferedWriter writer) {
        this.writer = writer;
        this.format = new Format();
    }

    public CSVWriter(BufferedWriter writer, Format format)
            throws FileNotFoundException {
        this.writer = writer;
        this.format = format;
    }

    public CSVWriter(String filename) {
        try {
            this.writer = new BufferedWriter(new FileWriter(new java.io.File(filename)));
            this.format = new Format();
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new CSVException(ex);
        }
    }

    public CSVWriter(java.io.File f) {
        try {
            this.writer = new BufferedWriter(new FileWriter(f));
            this.format = new Format();
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new CSVException(ex);
        }
    }

    public CSVWriter(OutputStream input) {
        writer = new BufferedWriter(new OutputStreamWriter(input));
        this.format = new Format();
    }

    public CSVWriter(String filename, Format format) {
        try {
            this.writer = new BufferedWriter(new FileWriter(filename));
            this.format = format;
        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new CSVException(ex);
        }
    }

    public CSVWriter(java.io.File f, Format format) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(f));
        this.format = format;
    }

    public CSVWriter(OutputStream input, Format format) {
        writer = new BufferedWriter(new OutputStreamWriter(input));
        this.format = format;
    }

    /**
     * Write a line to CSV<br/>
     * 
     * @throws IOException
     */
    public CSVWriter writeLine(Line line) throws CSVException {
        if (line == null) {
            return this;
        }

        synchronized (this) {

            // if current line index > 1
            // terminate previous line first
            if (row > 1) {
                carriageReturn();
            }

            if (!line.isEmpty()) {
                // try to write each cell in line
                try {
                    Object[] elements = line.getElements();
                    for (int i = 0; i < elements.length; i++) {
                        // not first element -> write field delimiter
                        if (i > 0) {
                            writer.append(format.getFieldDelimiter());
                        }
                        // write cell content
                        Object cell = elements[i];
                        if (cell != null) {
                            writer.append(escapeString(cell.toString()));
                        } else {
                            writer.append("");
                        }
                        row++;
                    }
                } catch (IOException e) {
                    throw new CSVException(e);
                }
            } else {
                row++;
            }
        }
        return this;
    }

    /**
     * Return carriage
     * 
     * @throws IOException
     */
    public CSVWriter carriageReturn() throws CSVException {
        try {
            synchronized (writer) {
                writer.append(format.getLineTerminator());
            }
        } catch (IOException e) {
            throw new CSVException("Error while returning carriage.", e);
        }
        return this;
    }

    /**
     * Write a file to underline CSV file
     * 
     * @param file
     */
    public void writeFile(File file) throws CSVException {
        if (file == null) {
            // no need to write
            return;
        }
        Line[] lines = file.getLines();
        synchronized (this) {
            for (int i = 0; i < lines.length; i++) {
                Line line = lines[i];
                writeLine(line);
            }
        }
    }

    /**
     * escape a string with text delimiter if needed
     * 
     * @param str
     * @return
     */
    private String escapeString(String str) {
        if (str.indexOf(format.getTextDelimiter()) >= 0
                || str.indexOf(format.getLineTerminator()) >= 0
                || str.indexOf(format.getFieldDelimiter()) >= 0) {
            return format.getTextDelimiter()
                    + str.replace(
                            "" + format.getTextDelimiter(),
                            "" + format.getTextDelimiter()
                                    + format.getTextDelimiter())
                    + format.getTextDelimiter();
        } else {
            // no need to escape
            return str;
        }
    }

    /**
     * close session
     */
    public void close() {
        try {
            flush();
        } catch (CSVException ex) {
            // do nothing
        }
        try {
            // auto flush
            if (writer != null) {
                writer.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    /**
     * Flush buffer to underline stream
     */
    public void flush() {
        try {
            synchronized (writer) {
                writer.flush();
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new CSVException("Cannot flush", ex);
        }
    }
}
