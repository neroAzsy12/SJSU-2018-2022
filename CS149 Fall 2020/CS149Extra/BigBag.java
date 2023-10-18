package cs149;
import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;

/** Azael Zamora
 *  NOTES:
 *  First 4 BYTES are the Magic Number, and it matches with the BIGBAG_MAGIC
 *  After getting the first 4 BYTES, convert the MappedByteBuffer to Little Endian (maybe)
 *
 *  For bigbag_hdr_s
 *      1. First 4 bytes are the magic number                   Position: 0 to 3
 *      2. The following 4 Bytes are first_free                 Position: 4 to 7
 *      3. The following 4 Bytes after are first_element        Position: 8 to 11
 *
 *  For bigbag_entry_s
 *      1. first 4 bytes, are the next bigbag entry             Position: 12 to 15
 *      2. followed by a byte that is either 0xDA or 0xF4       Position: 16
 *      3. followed by 3 bytes that serves as entry length (n)  Position: 17 to 19
 *      4. followed by n bytes that holds the str               Position: 20 to (20 + length)
 *
 */

public class BigBag {
    static int BIGBAG_SIZE = 65536;         // 64KB
    static int BIGBAG_MAGIC = 0xC5149BA9;   // Magic Number for BigBag file

    // gets the 3 bytes, and returns the length of the word in the bigbag entry
    public static int convert(byte[] b){
        return (b[0] & 255) << 16 | (b[1] & 255) << 8 | (b[2] & 255);
    }

    // lists the contents of the bigbag file
    public static void main(String[] args) {
        try(RandomAccessFile file = new RandomAccessFile(args[0], "r")){
            MappedByteBuffer memory_map = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, BIGBAG_SIZE);

            // gets the the first 4 bytes (0 to 3) of the MappedByteBuffer
            int magic_number = memory_map.getInt(0);

            // System.out.println("bigbag magic: " + BIGBAG_MAGIC);
            // System.out.println("file magic #: " + magic_number);

            // if the magic number does not match BIGBAG_MAGIC number, then it is not a bigbag file
            if(magic_number != BIGBAG_MAGIC){
                System.out.println("not a bigbag file");
                return;
            }

            // since the rest of the file is in Little Endian, convert the mem_map to LITTLE ENDIAN format when getting the offsets
            memory_map.order(ByteOrder.LITTLE_ENDIAN);

            // this gets Bytes (4 to 7), which is the first free entry in the BigBag file
            //int first_free = memory_map.getInt(4);
            //System.out.println("First free: " + first_free);

            // first element of the bigbag header is at position 8 (8 to 11)
            int current_offset = memory_map.getInt(8);
            //System.out.println("First element: " + current_offset);

            // if the bag is empty, exit the program, nothing will be printed
            if(current_offset == 0){
                // System.out.println("empty bag");
                return;
            }

            // iterates through the bigbag entries by their offsets and prints the elements
            while(current_offset != 0){
                //System.out.println("Offset: " + current_offset);    // prints the current offset of the bigbag entry

                // gets the length of the string in the current bigbag entry
                byte[] length_bytes = {memory_map.get(current_offset + 7), memory_map.get(current_offset + 6), memory_map.get(current_offset + 5)};
                int length = convert(length_bytes);
                //System.out.println("String length: " + length);

                StringBuilder sb = new StringBuilder();
                for(int i = current_offset + 8; i < current_offset + 8 + length; i++){
                    sb.append((char) memory_map.get(i));
                }

                System.out.println(sb.toString());

                current_offset = memory_map.getInt(current_offset);     // get the next offset for the next bigbag entry
            }
            //System.out.println("ends " + current_offset);

        }catch(Exception e){
            // 1. File does not exist
            // 2. Or if the size of the file is to small
            System.out.println("not a bigbag file");
        }

    }
}
