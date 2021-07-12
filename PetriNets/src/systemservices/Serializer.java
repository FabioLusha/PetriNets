package systemservices;

import java.io.IOException;

public interface Serializer<T> {
    void serialize(T object) throws IOException;
    T deserialize() throws IOException;
    boolean isEmpty();

}
