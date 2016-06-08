package archivo.digital.androidstart.utils;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public interface ResponseListener<T> {

    public void ok(T obj);
    public void err(String msg);
}
