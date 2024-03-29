package tw.kayjean.ui.server;

import java.io.IOException;

public interface RandomAccessInput
{
	public int readInt() throws IOException;
	
	public short readShort() throws IOException;
	
	public void readFully(byte[] a) throws IOException;

	public void seek(long n) throws IOException;
}