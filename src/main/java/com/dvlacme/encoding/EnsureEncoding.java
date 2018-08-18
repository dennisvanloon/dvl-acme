package com.dvlacme.encoding;

import org.springframework.stereotype.Service;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/**
 * Helper that takes a byte[] of chars and tries to guess the encoding
 *
  */
@Service
public class EnsureEncoding {
    public static final Charset[] TRY_ENC_UTF8_ISO88591_UTF16LE_UTF16BE = new Charset[] { UTF_8, ISO_8859_1, UTF_16LE, UTF_16BE };

    private final Charset[] encodingsToTry;

    /**
     * constructs with a set of encodings that works fine in most cases
     */
    public EnsureEncoding() {
        this(TRY_ENC_UTF8_ISO88591_UTF16LE_UTF16BE);
    }

    /**
     * @param encodingsToTry
     *          a set of charsets to be passed if the default ENCODINGS_TO_TRY does not give the expected results
     */
    public EnsureEncoding(Charset[] encodingsToTry) {
        this.encodingsToTry = encodingsToTry;
    }

    /**
     * will try to determine the charset of a byte array.
     *
     * @param chars
     *          characters in an unknown charset
     * @return the charset {@link Charset}
     */
    public Charset determineCharset(byte[] chars) {
        for (Charset encodingToTry : encodingsToTry) {
            try {
                decode(chars, encodingToTry);
                return encodingToTry;
            } catch (CharacterCodingException e) {
                // try with next encoding
            }
        }
        return null;
    }

    protected String decode(byte[] chars, Charset encodingToTry) throws CharacterCodingException {
        CharsetDecoder decoder = encodingToTry.newDecoder().onMalformedInput(CodingErrorAction.REPORT);

        ByteBuffer byteBuffer = ByteBuffer.wrap(chars);
        CharBuffer decoded = decoder.decode(byteBuffer);

        return decoded.toString();
    }

}