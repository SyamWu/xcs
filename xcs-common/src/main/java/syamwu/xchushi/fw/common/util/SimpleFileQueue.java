package syamwu.xchushi.fw.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import syamwu.xchushi.fw.common.Asset;

public class SimpleFileQueue<T> {

    private static final String SEPARATOR = System.getProperty("line.separator");

    private static final byte[] SEPARATOR_BYTES = SEPARATOR.getBytes();

    private static final int EMPTY_LEN = String.valueOf(Long.MAX_VALUE).length() + 1;

    private Lock lock = new ReentrantLock();

    private String charsetName = "";

    private String filePath = "";

    private RandomAccessFile rFile = null;

    public SimpleFileQueue(String fileName) throws IOException {
        this(fileName, "UTF-8");
    }

    public SimpleFileQueue(String filePath, String charsetName) throws IOException {
        this.charsetName = charsetName;
        URL url = SimpleFileQueue.class.getClassLoader().getResource(filePath);
        File file = null;
        if (url == null) {
            url = SimpleFileQueue.class.getClassLoader().getResource("");
            this.filePath = url.getFile() + filePath;
            file = new File(this.filePath);
            if (!file.exists()) {
                Asset.isTrue(FileUtil.createFile(file), "create file fail:" + this.filePath);
                clear();
            }
        } else {
            this.filePath = url.getFile();
            file = new File(this.filePath);
            if (!file.exists()) {
                Asset.isTrue(FileUtil.createFile(file));
                clear();
            }
        }
        this.rFile = new RandomAccessFile(this.filePath, "rw");
    }

//    public static void main(String[] args) {
//        String filePath = "eslogger/sdasd/dasds/test.txt";
//        URL url = SimpleFileQueue.class.getClassLoader().getResource(filePath);
//        if (url == null) {
//            url = SimpleFileQueue.class.getClassLoader().getResource("");
//            File file = new File(url.getFile() + filePath);
//            if (!file.exists()) {
//                System.out.println("创建文件:" + FileUtil.createFile(file));
//            }
//        }
//    }

    public String getFilePath() {
        return filePath;
    }

    public static List<String> getMessages(String fileName, boolean rePolled) throws IOException {
        return new SimpleFileQueue<String>(fileName).getMessages(true);
    }

    public List<String> getMessages() throws IOException {
        return getMessages(true);
    }

    public List<String> getMessages(boolean rePolled) throws IOException {
        List<String> list = null;
        try {
            lock.lock();
            rFile.seek(0);
            String offerIndexStr = rFile.readLine();
            String pollIndexStr = rFile.readLine();
            long pollIndex = 0l;
            if (rePolled) {
                if (pollIndexStr == null || pollIndexStr.length() < 1) {
                    throw new RuntimeException(filePath + " doesn't have pollIndex");
                } else {
                    pollIndex = Long.valueOf(pollIndexStr.trim());
                    if (pollIndex < 0) {
                        rFile.close();
                        throw new RuntimeException("pollIndex can't be less than 0");
                    }
                    rFile.seek(offerIndexStr.getBytes().length + SEPARATOR_BYTES.length + pollIndexStr.getBytes().length
                            + SEPARATOR_BYTES.length + pollIndex);
                }
            }
            String msgLenStr = rFile.readLine();
            while (msgLenStr != null && msgLenStr.length() > 0) {
                if (msgLenStr == null || msgLenStr.length() < 1) {
                    return null;
                }
                int msgLen = Integer.valueOf(msgLenStr);
                byte[] messageByte = new byte[msgLen];
                rFile.read(messageByte, 0, msgLen);
                rFile.read(new byte[SEPARATOR_BYTES.length]);
                if (list == null) {
                    list = new ArrayList<String>();
                }
                list.add(new String(messageByte, charsetName));
                msgLenStr = rFile.readLine();
            }
        } finally {
            lock.unlock();
        }
        return list;
    }

    public void offer(String message) throws IOException {
        try {
            lock.lock();
            rFile.seek(0);
            String offerIndexStr = rFile.readLine();
            String pollIndexStr = rFile.readLine();
            long offerIndex = 0l;
            if (offerIndexStr == null || offerIndexStr.length() < 1) {
                clear();
                rFile.seek(0);
                offerIndexStr = rFile.readLine();
                pollIndexStr = rFile.readLine();
                if (offerIndexStr == null || offerIndexStr.length() < 1) {
                    throw new RuntimeException(filePath + " doesn't have offerIndexStr");
                }
            } else {
                offerIndex = Long.valueOf(offerIndexStr.trim());
                rFile.seek(offerIndexStr.getBytes().length + SEPARATOR_BYTES.length + pollIndexStr.getBytes().length
                        + SEPARATOR_BYTES.length + offerIndex);
            }
            String content = message + SEPARATOR;
            byte[] contentBytes = content.getBytes(charsetName);
            String contentLen = String.valueOf(contentBytes.length - SEPARATOR_BYTES.length) + SEPARATOR;
            byte[] contentLenBytes = contentLen.getBytes(charsetName);
            rFile.write(contentLenBytes);
            rFile.write(contentBytes);
            rFile.seek(0);
            rFile.write(String.valueOf(offerIndex + contentLenBytes.length + contentBytes.length).getBytes());
        } finally {
            lock.unlock();
        }
    }

    public String poll() throws IOException {
        try {
            lock.lock();
            rFile.seek(0);
            String offerIndexStr = rFile.readLine();
            String pollIndexStr = rFile.readLine();
            long pollIndex = 0l;
            if (pollIndexStr == null || pollIndexStr.length() < 1) {
                throw new RuntimeException(filePath + " doesn't have pollIndex");
            } else {
                pollIndex = Long.valueOf(pollIndexStr.trim());
                if (pollIndex < 0) {
                    rFile.close();
                    throw new RuntimeException("pollIndex can't be less than 0");
                }
                rFile.seek(offerIndexStr.getBytes().length + SEPARATOR_BYTES.length + pollIndexStr.getBytes().length
                        + SEPARATOR_BYTES.length + pollIndex);
            }
            String msgLenStr = rFile.readLine();
            if (msgLenStr == null || msgLenStr.length() < 1) {
                return null;
            }
            int msgLen = Integer.valueOf(msgLenStr);
            byte[] messageByte = new byte[msgLen];
            rFile.read(messageByte, 0, msgLen);
            long newPollIndex = pollIndex + msgLenStr.getBytes("ISO-8859-1").length + SEPARATOR_BYTES.length + msgLen
                    + SEPARATOR_BYTES.length;
            long offerIndex = Long.valueOf(offerIndexStr.trim());
            rFile.seek(offerIndexStr.getBytes().length + SEPARATOR_BYTES.length);
            rFile.write(String.valueOf(newPollIndex).getBytes());
            if (newPollIndex >= offerIndex) {
                clear();
            }
            return new String(messageByte, charsetName);
        } finally {
            lock.unlock();
        }
    }

    public String peek() throws IOException {
        try {
            lock.lock();
            rFile.seek(0);
            String offerIndexStr = rFile.readLine();
            String pollIndexStr = rFile.readLine();
            long pollIndex = 0l;
            if (pollIndexStr == null || pollIndexStr.length() < 1) {
                throw new RuntimeException(filePath + " doesn't have pollIndex");
            } else {
                pollIndex = Long.valueOf(pollIndexStr.trim());
                if (pollIndex < 0) {
                    throw new RuntimeException("pollIndex can't be less than 0");
                }
                rFile.seek(offerIndexStr.getBytes().length + SEPARATOR_BYTES.length + pollIndexStr.getBytes().length
                        + SEPARATOR_BYTES.length + pollIndex);
            }
            String msgLenStr = rFile.readLine();
            if (msgLenStr == null || msgLenStr.length() < 1) {
                return null;
            }
            int msgLen = Integer.valueOf(msgLenStr);
            byte[] messageByte = new byte[msgLen];
            rFile.read(messageByte, 0, msgLen);
            return new String(messageByte, charsetName);
        } finally {
            lock.unlock();
        }
    }

    private void clear() throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
        RandomAccessFile rFile = new RandomAccessFile(filePath, "rw");
        rFile.write((String.valueOf(0) + getEmtyStr(EMPTY_LEN)).getBytes());
        rFile.write(SEPARATOR_BYTES);
        rFile.write((String.valueOf(0) + getEmtyStr(EMPTY_LEN)).getBytes());
        rFile.write(SEPARATOR_BYTES);
        rFile.close();
    }

    private String getEmtyStr(int len) {
        StringBuffer strBuff = new StringBuffer();
        for (int i = 0; i < len; i++) {
            strBuff.append(" ");
        }
        return strBuff.toString();
    }

}
