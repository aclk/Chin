package test.xsv.csv;

import java.util.ArrayList;
import java.util.List;

public class File {

    private List<Line> lines; // lines inside a CSV file

    /**
     * Default constructor
     */
    public File() {
        this.lines = new ArrayList<Line>();
    }

    public Line[] getLines() {
        return this.lines.toArray(new Line[0]);
    }

    /**
     * Get line at a specified index
     * 
     * @param idx
     * @return
     * @throws IndexOutOfBoundsException
     */
    public Line getLine(int idx) throws IndexOutOfBoundsException {
        return this.lines.get(idx);
    }

    /**
     * Add a new line to current csv
     * 
     * @return new line object
     */
    public Line newLine() {
        Line line = new Line();
        this.lines.add(line);
        return line;
    }

    /**
     * get number of line
     * 
     * @return number of line
     */
    public int size() {
        return this.lines.size();
    }

    /**
     * discard a line at the specified index
     * 
     * @param idx
     */
    public void discard(int idx) {
        if (idx >= 0 && idx < size()) {
            this.lines.remove(idx);
        }
    }

    /**
     * discard empty record
     */
    public void discardEmpty() {
        for (int i = this.lines.size() - 1; i > -1; i--) {
            if (this.lines.get(i) == null || this.lines.get(i).isEmpty()) {
                discard(i);
            }
        }
    }

    /**
     * append a CSV line to file (line is ignored if null)
     * 
     * @param line
     */
    public void append(Line line) {
        if (line == null) {
            return;
        }
        this.lines.add(line);
    }
}