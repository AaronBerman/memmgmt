package application;

public class memBlock {

    private int size;
    private String id;
    private String number;
    private int startIndex;
    private int endIndex;
    private boolean masterMemory;

    public memBlock() {
        size = 128;
        id = "Free";
        number = "0";
        startIndex = 0;
        endIndex = startIndex + size-1;
        masterMemory= false;
    }
    public memBlock(int x) {
        size = x;
        id = "Generic";
        number = "0";
        startIndex = 0;
        endIndex = startIndex + size-1;
        masterMemory= false;
    }
    public memBlock(int x, String y) {
        size = x;
        id = y;
        number = null;
        startIndex = 0;
        endIndex = startIndex + size-1;
        masterMemory= false;
    }

    public memBlock(int x, String y, String z) {
        size = x;
        id = y;
        number = z;
        startIndex = 0;
        endIndex = startIndex + size-1;
        masterMemory= false;
    }
    public memBlock(int x, String y, String z, int w) {
        size = x;
        id = y;
        number = z;
        startIndex = w;
        endIndex = startIndex + size-1;
        masterMemory= false;
    }

    public int getBlockSize() {
        return size;
    }
    public void setBlockSize(int x) { size = x; }
    public String getBlockId() { return  id; }
    public void setBlockId(String y) { id= y; }
    public String getProcessNumber() { return number; }
    public void setProcessNumber(String x) {number = x; }
    public int getStartIndex() { return startIndex; }
    public void setStartIndex(int w) {startIndex = w; size = endIndex - startIndex+1; }
    public int getEndIndex() { return endIndex; }
    public void setEndIndex(int w) { endIndex = w; size = endIndex - startIndex+1; }
    public void setMasterMemory(boolean x) {masterMemory = x;}
    public  boolean getMasterMemory() {return masterMemory;}
    @Override
    public String toString() {
        return ("Process: " + number + "    " + id + "    size:" + size) ;
    }
}
