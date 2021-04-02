package com.tody.SF;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferReaderCallback {
	Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
