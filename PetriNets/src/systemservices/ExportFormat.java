package systemservices;

public interface ExportFormat<T,E> {
    E format();
    T reverseFormat();

}
