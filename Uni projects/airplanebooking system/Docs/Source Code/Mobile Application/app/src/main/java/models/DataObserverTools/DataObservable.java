package models.DataObserverTools;

public interface DataObservable
{
    public void RegisterObserver(DataObserver o);
    public void RemoveObserver(DataObserver o);
    public void NotifyObservers();
}
