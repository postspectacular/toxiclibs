/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A collection of file handling utilities.
 */
public class FileUtils {

    /**
     * {@link FileDialog} constant
     */
    public static final int LOAD = FileDialog.LOAD;

    /**
     * {@link FileDialog} constant
     */
    public static final int SAVE = FileDialog.SAVE;

    /**
     * Attempts to create the full path of directories as specified by the given
     * target path. The path is assumed to be a directory, NOT a file in a
     * directory. For the latter, use {@link #createDirectoriesForFile(File)}.
     * 
     * @param path
     * @return true, if the operation succeeded
     */
    static public boolean createDirectories(File path) {
        try {
            if (!path.exists()) {
                path.mkdirs();
            }
            return true;
        } catch (SecurityException se) {
            System.err.println("No permissions to create "
                    + path.getAbsolutePath());
        }
        return false;
    }

    /**
     * Attempts to create the full path of directories as specified by the given
     * target file.
     * 
     * @param file
     * @return true, if the operation succeeded
     */
    static public boolean createDirectoriesForFile(File file) {
        try {
            String parentName = file.getParent();
            if (parentName != null) {
                File parent = new File(parentName);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
            }
            return true;
        } catch (SecurityException se) {
            System.err.println("No permissions to create "
                    + file.getAbsolutePath());
        }
        return false;
    }

    /**
     * Creates an {@link InputStream} for the given file. If the file extension
     * ends with ".gz" the stream is automatically wrapped in a
     * {@link GZIPInputStream} as well.
     * 
     * @param file
     *            input file
     * @return input stream
     * @throws IOException
     */
    static public InputStream createInputStream(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file can't be null");
        }
        InputStream stream = new FileInputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz")) {
            stream = new GZIPInputStream(stream);
        }
        return stream;
    }

    /**
     * Creates an {@link OutputStream} for the given file. If the file extension
     * ends with ".gz" the stream is automatically wrapped in a
     * {@link GZIPOutputStream} as well. Also attempts to create any
     * intermediate directories for the file using
     * {@link #createDirectoriesForFile(File)}.
     * 
     * @param file
     *            output file
     * @return output stream
     * @throws IOException
     */
    static public OutputStream createOutputStream(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file can't be null");
        }
        createDirectoriesForFile(file);
        OutputStream stream = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz")) {
            stream = new GZIPOutputStream(stream);
        }
        return stream;
    }

    /**
     * Creates a {@link BufferedReader} for the given file using UTF-8 encoding.
     * 
     * @param file
     * @return reader instance
     * @throws IOException
     */
    public static BufferedReader createReader(File file) throws IOException {
        return createReader(createInputStream(file));
    }

    /**
     * Creates a {@link BufferedReader} for the given {@link InputStream} using
     * UTF-8 encoding.
     * 
     * @param input
     *            stream
     * @return reader instance
     * @throws IOException
     */
    public static BufferedReader createReader(InputStream input) {
        return createReader(input, "UTF-8");
    }

    /**
     * Creates a {@link BufferedReader} for the given {@link InputStream} and
     * using the specified encoding.
     * 
     * @param input
     *            stream
     * @param encoding
     *            text encoding to use
     * @return reader instance
     * @throws IOException
     */
    public static BufferedReader createReader(InputStream input, String encoding) {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(input, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return new BufferedReader(isr, 0x10000);
    }

    /**
     * Creates a {@link BufferedWriter} for the given file using UTF-8 encoding.
     * 
     * @param file
     * @return writer instance
     * @throws IOException
     */
    public static BufferedWriter createWriter(File file) throws IOException {
        return createWriter(createOutputStream(file));
    }

    /**
     * Creates a {@link BufferedWriter} for the given {@link OutputStream} using
     * UTF-8 encoding.
     * 
     * @param out
     * @return writer instance
     * @throws IOException
     */
    public static BufferedWriter createWriter(OutputStream out) {
        return createWriter(out, "UTF-8");
    }

    /**
     * Creates a {@link BufferedWriter} for the given {@link OutputStream} and
     * using the specified encoding.
     * 
     * @param out
     *            stream
     * @param encoding
     *            text encoding to use
     * @return writer instance
     * @throws IOException
     */
    public static BufferedWriter createWriter(OutputStream out, String encoding) {
        OutputStreamWriter w = null;
        try {
            w = new OutputStreamWriter(out, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return new BufferedWriter(w, 0x10000);
    }

    /**
     * <p>
     * Analyses the given file path for a file sequence pattern and returns a
     * {@link FileSequenceDescriptor} instance for further use to handle this
     * sequence. The file pattern should be in one of these formats:
     * </p>
     * <ul>
     * <li>base_path-00001.ext</li>
     * <li>base_path001.ext</li>
     * </ul>
     * <p>
     * The sequence index should be using leading zeros, but the number of
     * digits will be identified automatically.
     * </p>
     * 
     * @param path
     *            file path of the first file in the sequence
     * @return descriptor, or null, if the path could not be analysed
     */
    public static FileSequenceDescriptor getFileSequenceDescriptorFor(
            String path) {
        int dotIndex = path.lastIndexOf('.');
        int zeroIndex = path.lastIndexOf('-') + 1;
        if (zeroIndex == 0) {
            zeroIndex = dotIndex - 1;
            while (path.charAt(zeroIndex) >= '0'
                    && path.charAt(zeroIndex) <= '9') {
                zeroIndex--;
            }
            zeroIndex++;
        }
        int numDigits = dotIndex - zeroIndex;
        if (dotIndex != -1 && numDigits > 0) {
            String base = path.substring(0, zeroIndex);
            String extension = path.substring(dotIndex);
            String filePattern = base + "%0" + numDigits + "d" + extension;
            int start = Integer.parseInt(path.substring(zeroIndex, dotIndex));
            return new FileSequenceDescriptor(filePattern, extension, dotIndex
                    - zeroIndex, start);
        } else {
            return null;
        }
    }

    /**
     * Loads the given {@link InputStream} into a byte array buffer.
     * 
     * @param stream
     * @return byte array
     * @throws IOException
     */
    static public byte[] loadBytes(InputStream stream) throws IOException {
        BufferedInputStream input = new BufferedInputStream(stream);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int c;
        while ((c = input.read()) != -1) {
            buffer.write(c);
        }
        return buffer.toByteArray();
    }

    /**
     * Loads the given {@link BufferedReader} as text, line by line into a
     * {@link String}.
     * 
     * @param reader
     * @return reader contents as string
     * @throws IOException
     */
    public static String loadText(BufferedReader reader) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    /**
     * Loads the given {@link InputStream} as text, line by line into a
     * {@link String} using UTF-8 encoding.
     * 
     * @param input
     *            stream
     * @return stream contents as string
     * @throws IOException
     */
    public static String loadText(InputStream input) throws IOException {
        return loadText(input, "UTF-8");
    }

    /**
     * Loads the given {@link InputStream} as text, line by line into a
     * {@link String} using the specified encoding.
     * 
     * @param input
     *            stream
     * @param encoding
     * @return stream contents as single string
     * @throws IOException
     */
    public static String loadText(InputStream input, String encoding)
            throws IOException {
        byte[] raw = loadBytes(input);
        return new String(raw, encoding);
    }

    /**
     * Saves the given text to the specified {@link BufferedWriter} instance,
     * then closes the writer afterwards.
     * 
     * @param writer
     * @param string
     * @throws IOException
     */
    static public void saveText(BufferedWriter writer, String string)
            throws IOException {
        writer.write(string);
        writer.flush();
        writer.close();
    }

    /**
     * Saves the given text to the specified {@link OutputStream} instance, then
     * closes the underlying writer afterwards.
     * 
     * @param output
     * @param string
     * @throws IOException
     */
    static public void saveText(OutputStream output, String string)
            throws IOException {
        saveText(createWriter(output), string);
    }

    /**
     * Displays a standard AWT file dialog for choosing a folder (no files!).
     * 
     * @param frame
     *            parent frame
     * @param title
     *            dialog title
     * @param path
     *            base directory (or null)
     * @return path to chosen directory or null, if user has canceled
     */
    public static String showDirectoryChooser(final Frame frame,
            final String title, String path) {
        System.setProperty("apple.awt.fileDialogForDirectories", "true");
        String result = showFileDialog(frame, title, path,
                new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return new File(dir + "/" + name).isDirectory();
                    }
                }, LOAD);
        System.setProperty("apple.awt.fileDialogForDirectories", "false");
        return result;
    }

    /**
     * Displays a standard AWT file dialog for choosing a file for loading or
     * saving.
     * 
     * @param frame
     *            parent frame
     * @param title
     *            dialog title
     * @param path
     *            base directory (or null)
     * @param filter
     *            a FilenameFilter implementation (or null)
     * @param mode
     *            either FileUtils.LOAD or FileUtils.SAVE
     * @return path to chosen file or null, if user has canceled
     */
    public static String showFileDialog(final Frame frame, final String title,
            String path, FilenameFilter filter, final int mode) {
        String fileID = null;
        FileDialog fd = new FileDialog(frame, title, mode);
        if (path != null) {
            fd.setDirectory(path);
        }
        if (filter != null) {
            fd.setFilenameFilter(filter);
        }
        fd.setVisible(true);
        if (fd.getFile() != null) {
            fileID = fd.getFile();
            fileID = fd.getDirectory() + fileID;
        }
        return fileID;
    }

    /**
     * Displays a standard AWT file dialog for choosing a file for loading or
     * saving.
     * 
     * @param frame
     *            parent frame
     * @param title
     *            dialog title
     * @param path
     *            base directory (or null)
     * @param formats
     *            an array of allowed file extensions (or null to allow all)
     * @param mode
     *            either FileUtils.LOAD or FileUtils.SAVE
     * @return path to chosen file or null, if user has canceled
     */
    public static String showFileDialog(final Frame frame, final String title,
            String path, final String[] formats, final int mode) {
        String fileID = null;
        FileDialog fd = new FileDialog(frame, title, mode);
        if (path != null) {
            fd.setDirectory(path);
        }
        if (formats != null) {
            fd.setFilenameFilter(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    boolean isAccepted = false;
                    for (String ext : formats) {
                        if (name.indexOf(ext) != -1) {
                            isAccepted = true;
                            break;
                        }
                    }
                    return isAccepted;
                }
            });
        }
        fd.setVisible(true);
        if (fd.getFile() != null) {
            fileID = fd.getFile();
            fileID = fd.getDirectory() + fileID;
        }
        return fileID;
    }
}
