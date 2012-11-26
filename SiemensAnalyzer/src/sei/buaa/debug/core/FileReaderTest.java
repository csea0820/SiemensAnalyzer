package sei.buaa.debug.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import sei.buaa.debug.entity.Timer;

public class FileReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new FileReaderTest().go();
	}
	
	public List<File> splitFiles(File[] files,int start,int interval)
	{
		
		List<File> list = new ArrayList<File>();
		
		while (start < files.length)
		{
			list.add(files[start]);
			
			start += interval;
		}
		
		return list;
		
	}
	
	public void go()
	{
		
		
		File dir = new File("/Users/csea/Documents/Experiment/Siemens/replace/outputs/v2");

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			
			Timer t2 = new Timer();
			t2.start();
			
			List<MyThread> threads = new ArrayList<MyThread>();
			
			for (int i = 0; i < 7; i++)
			{
				List<File> fs = splitFiles(files,i,7);
				MyThread myThread = new MyThread(fs);
				threads.add(myThread);
				myThread.start();
			}
			
			for (MyThread thread:threads)
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			t2.end();
			t2.timeElapse("bufferedReader");
			
			
			
			
		} else {
		}
		
		
//		File dir1 = new File("/Users/csea/Documents/Experiment/Siemens/replace/outputs/v1");
//
//		Timer t1 = new Timer();
//		t1.start();
//		
//		for (File file : dir1.listFiles()) {
//			fileChannel(file.getAbsolutePath());
//		}
//		
//		t1.end();
//		t1.timeElapse("fileChannel");
		
		
	}

	private void bufferedReader(String absolutePath) {
			
		
		BufferedReader br = null;
		FileReader fr = null;
		String str;
		
		try {
			fr = new FileReader(new File(absolutePath));
			br = new BufferedReader(fr);

			str = br.readLine();
			while (str != null) {
				
				
				str = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void fileChannel(String absolutePath) {
		
		File file = new File(absolutePath);

		MappedByteBuffer out = null;
		FileChannel fc = null;
		RandomAccessFile raf = null;
		long length = file.length();
		
		try {
			raf = new RandomAccessFile(absolutePath, "r");
			fc = raf.getChannel();
			out = fc.map(FileChannel.MapMode.READ_ONLY, 0, length);
			int len = out.capacity();
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < len; i++) {
				if (out.get(i) == 10) {
				
					result = new StringBuilder();
				}
				else
				{
					result.append((char) out.get(i));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
//			if(out != null){
                try {
					fc.close();
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//                out.force();
//                out = null;
//                System.gc();
//			}
		}
		
	}

}

class MyThread extends Thread{
	
	private List<File> files = null;
	
	public MyThread(List<File> files)
	{
		this.files = files;
	}
	
	public void go(File file)
	{
		BufferedReader br = null;
		FileReader fr = null;
		String str;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			str = br.readLine();
			while (str != null) {
				
				
				str = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run()
	{
		for (File file:files)
			go(file);
	}
	
}
