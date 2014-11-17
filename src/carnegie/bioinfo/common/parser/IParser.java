package carnegie.bioinfo.common.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IParser {
    public int parse() throws IOException;
    public HashMap getResultHashMap();
    public ArrayList getResultCollection();
}
