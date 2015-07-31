package spiritstoolkit.dbsettings.data;

import java.util.StringTokenizer;

public class HistoryData {

    private static final String DELIM = "#";

    private String jdbcUrl = "";

    private String user = "";

    private String password = "";

    private String schema = "";

    public HistoryData(String jdbcUrl, String user, String password,
        String schema) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.schema = schema;
    }

    public HistoryData(String line) {
        StringTokenizer token = new StringTokenizer(line, DELIM);
        if (token.hasMoreTokens()) {
            jdbcUrl = token.nextToken();
        }
        if (token.hasMoreTokens()) {
            user = token.nextToken();
        }
        if (token.hasMoreTokens()) {
            password = token.nextToken();
        }
        if (token.hasMoreTokens()) {
            schema = token.nextToken();
        }
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public String getHistoryData() {
        return jdbcUrl + DELIM + user + DELIM + password + DELIM + schema;
    }

    public boolean equals(Object obj) {
        if (obj instanceof HistoryData) {
            HistoryData dest = ((HistoryData)obj);
            return jdbcUrl.equals(dest.getJdbcUrl())
                && user.equals(dest.getUser())
                && password.equals(dest.getPassword())
                && schema.equals(dest.getSchema());
        }
        return false;
    }

    public String toString() {
        return "<html><table border=2 width=500><tr><td width=50>JdbcUrl</td><td>"
            + jdbcUrl
            + "</td></tr><tr><td width=50>User</td><td>"
            + user
            + "</td></tr></table></html>";
    }
}
