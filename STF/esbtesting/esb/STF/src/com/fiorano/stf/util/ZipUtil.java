package com.fiorano.stf.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipOutputStream;

/**
 * @author Sandeep M
 * @Date:
 */
public class ZipUtil {
    private static boolean DEBUG = false;

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        ZipUtil.DEBUG = DEBUG;
    }

    /**
     * Zip a directory and writes it on parameter ZipOutStream.
     *
     * @param bufferSize specifies the size with which data is being read.
     * @param dir2zip
     * @param filter
     * @param zos
     * @throws java.io.IOException
     */
    public static void zipDir(String dir2zip, FilenameFilter filter,
                              java.util.zip.ZipOutputStream zos,
                              int bufferSize)
            throws IOException {
        // create a new File object based on the directory we have to zip
        File zipDir = new File(dir2zip);

        zipDir(zipDir, zipDir, filter, zos, bufferSize);
    }


    /**
     * Unzips a zipFile in unzip directory.
     *
     * @param zip
     * @param unzipDir
     * @param bufferSize
     * @throws IOException
     */
    public static void unzip(String zip, String unzipDir, int bufferSize)
            throws IOException {
        // Initialize the unzip Dir.
        createDirs(unzipDir);

        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zip);
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            java.util.zip.ZipEntry entry = (java.util.zip.ZipEntry)
                    entries.nextElement();

            // File
            File file = new File(unzipDir, entry.getName());

            if (entry.isDirectory()) {
                // Assume directories are stored parents first then children.
                if (DEBUG)
                    System.out.println("Extracting directory: " + entry.getName());

                if (!file.exists()) {
                    boolean created = file.mkdir();

                    if (DEBUG)
                        System.out.println("Directory " + file +
                                " created /? " + created);
                }

                continue;
            }

            if (DEBUG)
                System.out.println("Extracting file: " + entry.getName());

            // write entry on file-system.
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream entryStream = zipFile.getInputStream(entry);

            copyInputStream(entryStream, bos, bufferSize);
        }

        zipFile.close();
    }

    /**
     * Zip a directory, where relative path is relativeDir
     *
     * @param bufferSize specifies the size with which data is being read.
     * @param zipDir
     * @param baseDir
     * @param filter
     * @param zos
     * @throws IOException
     */
    private static void zipDir(File zipDir, File baseDir, FilenameFilter filter,
                               java.util.zip.ZipOutputStream zos,
                               int bufferSize)
            throws IOException {
        if (zipDir.isFile()) {
            //File file = zipDir;
            String entryName = entryName(zipDir, baseDir);

            // add zip entry
            _addZipEntry(zos, entryName);

            // add file to zip strea,
            _addFileToZipStream(zipDir, zos, bufferSize);
        } else {
            //get a listing of the directory content
            String[] dirList = zipDir.list(filter);

            //loop through dirList, and zip the files
            for (int i = 0; i < dirList.length; i++) {
                File file = new File(zipDir, dirList[i]);

                if (file.isDirectory()) {
                    String entryName = entryName(file, baseDir);

                    // add zip entry
                    _addZipEntry(zos, entryName);
                }

                zipDir(file, baseDir, filter, zos, bufferSize);
            }
        }
    }

    /**
     * Add file to parameter zipStream
     *
     * @param file
     * @param zos
     * @param bufferSize
     * @throws IOException
     */
    private static void _addFileToZipStream(File file, ZipOutputStream zos,
                                            int bufferSize)
            throws IOException {
        byte[] readBuffer = new byte[bufferSize];
        int bytesIn = 0;

        if (file.canRead()) {
            java.io.FileInputStream fis = new java.io.FileInputStream(file);

            try {
                // write the content of the file to the ZipOutputStream
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }

                // Close the Stream
                fis.close();
            }
            catch (IOException ex) {
                if (bytesIn != 0)
                    throw ex;

                System.out.println("Skipping file -> " + file.getName());
                // Ignore the exception
            }

        } else
            System.out.println("Unable to read file " + file.getName());
    }

    private static void _addZipEntry(ZipOutputStream zos, String entryName)
            throws IOException {
        // create a new zip entry
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entryName);

        if (DEBUG)
            System.out.println("Entry name -> " + entryName);

        // place the zip entry in the ZipOutputStream object
        zos.putNextEntry(zipEntry);
    }


    /**
     * Converts file pathname to a form acceptable to ZIP files.
     * In particular, file separators are converted to forward slashes.
     *
     * @param file        the file to be converted.
     * @param relativeDir the dir which will be referenced when
     *                    caculates the returned file path
     * @return a relative path of the given file according to
     *         the specified relativeDir.
     *         <p/>
     *         For exsample: - If the absolute path of a file
     *         is e:\\a\\b\\c\\file.suf, and the relativeDir is
     *         e:\\a\\b, then "c/file.suf" will be returned.
     *         <p/>
     *         if the String which presents a path name contains
     *         "\\" as separator, "\\" will be replaced by "/"
     */
    private static String entryName(File file, File relativeDir) {
        if (relativeDir.isFile())
            return file.getName();

        String fileName = file.getAbsolutePath();
        String relativePath = relativeDir.getAbsolutePath();

        if (!relativePath.endsWith(File.separator)) {
            relativePath += File.separator;
        }
        fileName = fileName.substring(relativePath.length());

        if ((file.isDirectory()) && (!fileName.endsWith(File.separator)))
            fileName += File.separator;

        return fileName.replace(File.separatorChar, '/');
    }

    public static boolean createDirs(String dirName) {
        File dir = new File(dirName);

        if (dir.exists())
            return true;

        boolean created = createDirs(dir.getParent());

        if (!created)
            return false;

        return dir.mkdir();
    }


    private final static void copyInputStream(InputStream in, OutputStream out, int bufferSize)
            throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;

        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }
}
