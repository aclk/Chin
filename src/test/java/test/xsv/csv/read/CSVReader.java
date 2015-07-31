package test.xsv.csv.read;

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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import test.xsv.csv.CSVException;
import test.xsv.csv.File;
import test.xsv.csv.Format;
import test.xsv.csv.Line;

/**
 * CSV file reader
 * 
 * @author Takaji
 */
public final class CSVReader {

    private Format format;
    private final BufferedReader reader;
    private int col = 1;
    private int row = 1;
    private StringBuilder element = new StringBuilder(); // store current
    // element
    private Line currentLine = null; // current CSV line
    private boolean isOpened = false; // a string is opened with text delimiter

    public CSVReader(Reader reader) {
        // validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = new BufferedReader(reader);
        this.format = new Format();
    }

    public CSVReader(Reader reader, Format format) {
        // validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = new BufferedReader(reader);
        this.format = format;
    }

    public CSVReader(BufferedReader reader) throws FileNotFoundException {
        // validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = reader;
        this.format = new Format();
    }

    public CSVReader(BufferedReader reader, Format format) {
        // validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = reader;
        this.format = format;
    }

    public CSVReader(String filename) throws CSVException {
        // validate parameter
        checkNotNull(filename, "File name cannot be empty");
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = new Format();
    }

    public CSVReader(java.io.File f) {
        // validate parameter
        checkNotNull(f, "File object cannot be null");
        try {
            this.reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = new Format();
    }

    public CSVReader(InputStream input) {
        // validate parameter
        checkNotNull(input, "InputStream cannot be null");
        reader = new BufferedReader(new InputStreamReader(input));
        this.format = new Format();
    }

    public CSVReader(String filename, Format format) {
        // validate parameter
        checkNotNull(filename, "File name cannot be empty");
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = format;
    }

    public CSVReader(java.io.File f, Format format) {
        // validate parameter
        checkNotNull(f, "File object cannot be null");
        try {
            this.reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = format;
    }

    public CSVReader(InputStream input, Format format) {
        // validate parameter
        checkNotNull(input, "InputStream cannot be null");
        reader = new BufferedReader(new InputStreamReader(input));
        this.format = format;
    }

    /**
     * read next char
     */
    private int read() throws IOException {
        int next = -1;
        next = reader.read();
        if (next != -1) {
            col++;
        }
        return next;
    }

    /**
     * Do carriage return
     */
    private void carriageReturn() {
        synchronized (this) {
            row++;
            col = 1;
        }
    }

    /**
     * Finished read a cell, flush to current line
     */
    private void flush() {
        currentLine.add(element.toString());
        element.setLength(0); // reset current element
        isOpened = false;
    }

    /**
     * Parse a CSV string to data
     * 
     * @param rawData
     * @return a simple CSV object with data. null if the raw data cannot be
     *         parse
     * @throw CSVException
     */
    public Line readLine() throws CSVException {
        if (reader == null) {
            throw new CSVException("No reader found.");
        }
        synchronized (this) {
            element = new StringBuilder(); // store current
            // element
            currentLine = null; // current CSV line
            isOpened = false; // a string is opened with text delimiter

            try {
                int currentInt = -1;
                // parse data
                while ((currentInt = read()) != -1) {
                    if (currentLine == null) {
                        currentLine = new Line();
                    }

                    // reach field delimiter
                    if (currentInt == format.getTextDelimiter()) {
                        if (isOpened) {
                            // check next char is escape?
                            if ((currentInt = read()) != -1) {
                                if (currentInt == format.getTextDelimiter()) {
                                    // is special char
                                    element.append(format.getTextDelimiter());
                                    continue;
                                } else {
                                    // is close string then flush
                                    flush();
                                    // next char must be field delimiter
                                    // or a line terminator
                                    if (currentInt == format
                                            .getFieldDelimiter()) {
                                        continue;
                                    } else if (currentInt == format
                                            .getLineTerminator()) {
                                        carriageReturn();
                                        return currentLine;
                                    } else {
                                        raiseExpected(
                                                format.getFieldDelimiter(),
                                                (char) currentInt);
                                    }
                                    continue;
                                }
                            } else {
                                flush();
                                continue;
                            }
                        } else {
                            // is first open of the string
                            isOpened = true;
                            continue;
                        }
                    }

                    if (!isOpened) {
                        // ignore character
                        if (format.isIgnored((char) currentInt)) {
                            continue; // just ignore
                        } else if (currentInt == format.getLineTerminator()) {
                            flush();

                            if (currentLine != null) {
                                carriageReturn();
                            }
                            return currentLine;
                        }
                    }

                    // meet field delimiter
                    if (currentInt == format.getFieldDelimiter()) {
                        if (isOpened) {
                            element.append((char) currentInt);
                            continue;
                        } else {
                            flush();
                            continue;
                        }
                    }

                    // otherwise, it's a normal character, just add
                    element.append((char) currentInt);
                }// continue while

                // flush last element if needed
                if (element != null && currentLine != null) {
                    flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                raiseException(ex);
            }
            // if currentLine exists, return carriage
            if (currentLine != null) {
                carriageReturn();
            }
        }// end of synchronize
        return currentLine;
    }

    /**
     * Expected char [expected] but found another char [actual] so we throw an
     * exception here.
     * 
     * @param expected
     * @param actual
     */
    private void raiseExpected(char expected, char actual) {
        raiseException(new Exception(String.format(
                "Invalid data. Expected [%s] but [%s] was found.",
                String.valueOf(expected), String.valueOf(actual))));
    }

    private void raiseException(Exception ex) {
        throw new CSVException("Error happened while processing at row: " + row
                + " - col: " + (col - 1) + ")", ex);
    }

    /**
     * Check if argument null then throw a new CSV Exception
     * 
     * @param argument
     */
    public static void checkNotNull(Object argument, String message)
            throws CSVException {
        if (argument == null) {
            throw new CSVException(message);
        }
    }

    /**
     * Close current reader and underline stream
     */
    public void close() {
        try {
            synchronized (reader) {
                reader.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    /**
     * Parse a CSV stream to data
     * 
     * @param stream
     *            InputStream
     * @return a simple CSV object with data. null if the raw data cannot be
     *         parse
     */
    public static File parse(InputStream stream) throws CSVException {
        try {
            CSVReader reader = new CSVReader(stream);
            File file = new File();
            Line line = null;
            do {
                line = reader.readLine();
                file.append(line);
            } while (line != null);
            return file;
        } catch (Exception ex) {
            throw new CSVException(ex);
        }
    }

    /**
     * Parse file content to CSV
     * 
     * @param path
     * @return
     */
    public static File parseFile(String path) {
        if (path == null || !(new java.io.File(path)).exists()) {
            throw new CSVException("Invalid path");
        }
        try {
            return parse(new FileInputStream(path));
        } catch (Exception ex) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException(ex);
        }
    }
}