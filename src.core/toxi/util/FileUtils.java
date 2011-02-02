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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A collection of file handling utilities.
 */
public class FileUtils {

    static public void createDirectories(File file) {
        try {
            String parentName = file.getParent();
            if (parentName != null) {
                File parent = new File(parentName);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
            }
        } catch (SecurityException se) {
            System.err.println("No permissions to create "
                    + file.getAbsolutePath());
        }
    }

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

    static public OutputStream createOutputStream(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("file can't be null");
        }
        createDirectories(file);
        OutputStream stream = new FileOutputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz")) {
            stream = new GZIPOutputStream(stream);
        }
        return stream;
    }

    public static BufferedReader createReader(File file) throws IOException {
        return createReader(createInputStream(file));
    }

    public static BufferedReader createReader(InputStream input) {
        return createReader(input, "UTF-8");
    }

    public static BufferedReader createReader(InputStream input, String encoding) {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(input, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return new BufferedReader(isr);
    }

    public static PrintWriter createWriter(File file) throws IOException {
        return createWriter(createOutputStream(file));
    }

    public static PrintWriter createWriter(OutputStream out) {
        return createWriter(out, "UTF-8");
    }

    public static PrintWriter createWriter(OutputStream out, String encoding) {
        OutputStreamWriter w = null;
        try {
            w = new OutputStreamWriter(out, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return new PrintWriter(w);
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

    static public byte[] loadBytes(InputStream stream) throws IOException {
        BufferedInputStream input = new BufferedInputStream(stream);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int c;
        while ((c = input.read()) != -1) {
            buffer.write(c);
        }
        return buffer.toByteArray();
    }

    public static String loadText(BufferedReader r) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    public static String loadText(InputStream input) throws IOException {
        return loadText(input, "UTF-8");
    }

    public static String loadText(InputStream input, String encoding)
            throws IOException {
        byte[] raw = loadBytes(input);
        return new String(raw, encoding);
    }

    static public void saveText(OutputStream output, String string) {
        saveText(createWriter(output), string);
    }

    static public void saveText(PrintWriter writer, String string) {
        writer.println(string);
        writer.flush();
        writer.close();
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
     *            either FileDialog.LOAD or FileDialog.SAVE
     * @return path to chosen file or null, if user has cancelled
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
