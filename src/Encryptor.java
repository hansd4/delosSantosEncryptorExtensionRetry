import java.util.Arrays;

public class Encryptor {
    /** The Alphabet, uppercase */
    private final String UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** The Alphabet, lowercase */
    private final String LOWER_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /** A two-dimensional array of single-character strings, instantiated in the constructor */
    private String[][] letterBlock;

    /** The number of rows of letterBlock, set by the constructor */
    private int numRows;

    /** The number of columns of letterBlock, set by the constructor */
    private int numCols;

    /** The number of elements of letterBlock, as in, it's total 2D size. Calculated from numRows and numCols */
    private int blockSize;

    /** The character shift used for encryption, as an integer. Positive values shift all letters up in the alphabet, negative values shift down. Set by the constructor */
    private int charShift;

    /** The shift in rows used for encryption, as an integer. Set by the constructor. */
    private int rowShift;

    /** The shift in columns used for encryption, as an integer. Set by the constructor. */
    private int colShift;

    /** Constructor*/
    public Encryptor(int key) {
        int[] vars = keyToIntValues(key);
        numRows = vars[0];
        numCols = vars[1];
        charShift = vars[2];
        rowShift = vars[3];
        colShift = vars[4];
        letterBlock = new String[numRows][numCols];
        blockSize = numRows * numCols;
    }

    public String[][] getLetterBlock() {
        return letterBlock;
    }

    /** Places a string into letterBlock in row-major order.
     *
     *   @param str  the string to be processed
     *
     *   Postcondition:
     *     if str.length() < numRows * numCols, "A" in each unfilled cell
     *     if str.length() > numRows * numCols, trailing characters are ignored
     */
    public void fillBlock(String str) {
        if (str.length() < blockSize) {
            for (int i = str.length(); i < blockSize; i++) {
                str += "A";
            }
        }

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                letterBlock[r][c] = str.substring(r*numCols + c, r*numCols + c + 1);
            }
        }
    }

    /** Extracts encrypted string from letterBlock in column-major order.
     *
     *   Precondition: letterBlock has been filled
     *
     *   @return the encrypted string from letterBlock
     */
    public String encryptBlock() {
        String encryptedString = "";
        for (int c = 0; c < numCols; c++) {
            for (int r = 0; r < numRows; r++) {
                encryptedString += letterBlock[r][c];
            }
        }
        return encryptedString;
    }

    /** Encrypts a message.
     *
     *  @param message the string to be encrypted
     *
     *  @return the encrypted message; if message is the empty string, returns the empty string
     */
    public String encryptMessage(String message) {
        for (int i = 0; i < message.length() % blockSize; i++) {
            message += "A";
        }

        String encryptedMessage = "";
        for (int i = 0; i < message.length(); i += blockSize) {
            fillBlock(message.substring(i, i + blockSize));
            encryptedMessage += encryptBlock();
        }
        return encryptedMessage;
    }

    /**  Decrypts an encrypted message. All filler 'A's that may have been
     *   added during encryption will be removed, so this assumes that the
     *   original message (BEFORE it was encrypted) did NOT end in a capital A!
     *
     *   NOTE! When you are decrypting an encrypted message,
     *         be sure that you have initialized your Encryptor object
     *         with the same row/column used to encrypted the message! (i.e.
     *         the “encryption key” that is necessary for successful decryption)
     *         This is outlined in the precondition below.
     *
     *   Precondition: the Encryptor object being used for decryption has been
     *                 initialized with the same number of rows and columns
     *                 as was used for the Encryptor object used for encryption.
     *
     *   @param encryptedMessage  the encrypted message to decrypt
     *
     *   @return  the decrypted, original message (which had been encrypted)
     *
     *   TIP: You are encouraged to create other helper methods as you see fit
     *        (e.g. a method to decrypt each section of the decrypted message,
     *         similar to how encryptBlock was used)
     */
    public String decryptMessage(String encryptedMessage) {
        String decryptedMessage = "";
        for (int i = 0; i < encryptedMessage.length(); i += blockSize) {
            decryptString(encryptedMessage.substring(i, i + blockSize));
            decryptedMessage += decryptBlock();
        }
        return decryptedMessage;
    }

    public String superEncryptMessage(String message) {
        for (int i = 0; i < message.length() % blockSize; i++) {
            message += "A";
        }

        String encryptedMessage = "";
        for (int i = 0; i < message.length(); i += blockSize) {
            fillBlock(message.substring(i, i + blockSize));
            encryptedMessage += encryptBlock();
        }
        return encryptedMessage;
    }

    private void decryptString(String encryptedString) {
//        System.out.println("String to add to letterBlock: " + encryptedString);
        for (int c = 0; c < numCols; c++) {
            for (int r = 0; r < numRows; r++) {
                letterBlock[r][c] = encryptedString.substring(c*numRows + r, c*numRows + r + 1);
            }
        }

//        System.out.println();
//        System.out.println("letterBlock: ");
//        for (String[] row : letterBlock) {
//            for (String s : row) {
//                System.out.print(s + "|");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }

    private String decryptBlock() {
        String decryptedString = "";
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                decryptedString += letterBlock[r][c];
            }
        }
        while (decryptedString.endsWith("A")) {
            decryptedString = decryptedString.substring(0, decryptedString.length() - 1);
        }
        return decryptedString;
    }

    private int[] keyToIntValues(int key) {
        int[] values = new int[5];
        int i = 4;
        while (i >= 0) {
            values[i] = key % 10;
            key /= 10;
            i++;
        }
        return values;
    }

    private String shiftString(String s) {

    }

    private String shiftChar(String c) {
        int upperIndex = c.indexOf(UPPER_ALPHABET);
        if (upperIndex == -1) {
            int lowerIndex = c.indexOf(LOWER_ALPHABET);
            if (lowerIndex == -1) { // not a letter
                return c;
            } else { // lowercase letter
                lowerIndex = (lowerIndex + charShift) % 26;
                return LOWER_ALPHABET.substring(lowerIndex, lowerIndex + 1);
            }
        } else { // uppercase letter
            upperIndex = (upperIndex + charShift) % 26;
            return UPPER_ALPHABET.substring(upperIndex, upperIndex + 1);
        }
    }
}

