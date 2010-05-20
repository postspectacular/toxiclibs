package toxi.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;

/**
 * A collection of file handling utilities.
 */
public class FileUtils {

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
        int zeroIndex = path.indexOf('-') + 1;
        if (zeroIndex == 0) {
            zeroIndex = path.indexOf('0');
        }
        String base = path.substring(0, zeroIndex);
        int dotIndex = path.indexOf('.', zeroIndex);
        if (dotIndex != -1) {
            String extension = path.substring(dotIndex);
            String filePattern =
                    base + "%0" + (dotIndex - zeroIndex) + "d" + extension;
            int start = Integer.parseInt(path.substring(zeroIndex, dotIndex));
            return new FileSequenceDescriptor(filePattern, extension, dotIndex
                    - zeroIndex, start);
        } else {
            return null;
        }
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
