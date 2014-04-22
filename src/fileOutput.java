import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


class fileOutput
{
	public static String vKeyFilePath, readmode, mapFilePath;
	public static RandomAccessFile finput = null;
	public static RandomAccessFile foutput = null;
	public static Integer block[];
	public static int kgMap[][];

	public static void buildKeyGraphMap(String rPath, String wPath,
			String mode, int debug) throws IOException // ����ʵ�ְ��ж�ȡÿ����������ؼ��ʵ���Ϣ
	{
		vKeyFilePath = rPath;
		mapFilePath = wPath;
		readmode = mode;
		kgMap = new int[matrix.mSize * 100][matrix.mSize + 1];//2ά���飬Ϊ1��ʾ�ùؼ��ʰ����ڸ���ͼ��

		for (int i = 0; i <= matrix.mSize * 100 - 1; i++)
			for (int j = 0; j <= matrix.mSize - 1; j++)
				kgMap[i][j] = -1;

		ArrayList<Integer> bufferlist = new ArrayList<Integer>();
		String temp[];
		String inString = null;

		try
		{
			finput = new RandomAccessFile(vKeyFilePath, mode);
			foutput = new RandomAccessFile(mapFilePath, mode);
		}
		catch (FileNotFoundException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

		int vNum;// ��ʾ��ǰ���ڶ�ȡ�����ĸ������������Ĺؼ��ʣ���0�Žڵ㿪ʼ
		int keyNum, graphNum, graphCount, seekNum;
		
		//�Ӷ���--�ؼ��ʵ��ļ��а���
		for (inString = finput.readLine(), vNum = 0; inString != null; inString = finput
				.readLine(), vNum++)
		{
			temp = inString.split(" "); // ��vNnum�ڵ��еĹؼ�����������ȡ����,temp��String ������

			bufferlist = preProcess.vGraphMap.get(vNum);	// ������vNum������r�뾶��ͼ��������
			if (debug == 1)
			{
				System.out.println("�ڶ��㣺" + vNum);
				System.out.println("�ؼ��� size=" + temp.length);
				System.out.println("�ö����Ӧ��ͼ���� " + bufferlist.size());
			}

			for (int i = 0; i <= temp.length - 1; i++)
			{
				keyNum = Integer.parseInt(temp[i]);
				if(debug == 1)
					System.out.println("");
				for (int t = 0; t <= bufferlist.size() - 1; t++)
				{
					graphNum = bufferlist.get(t);
					if (debug == 1)
						System.out.println("keynum : " + keyNum + " graphnum: "
								+ graphNum);
					kgMap[keyNum][graphNum] = 1;
				}
				kgMap[keyNum][matrix.mSize] = 1; // ����������һ��д��1 ��ʾ����������

			
			}
			if(debug == 1)
				System.out.println();

		}
		// breakflag = 0;
		if (debug == 1)
		{
			for (int i = 0; i <= matrix.mSize * 100 - 1; i++)
			{
				if (kgMap[i][matrix.mSize] != 1)
				{
					// System.out.print("break on :"+ i);
					break;
				}

				for (int j = 0; j <= matrix.mSize - 1; j++)
				{

					if (j != matrix.mSize - 1)
						System.out.print(kgMap[i][j] + " ");
					else
						System.out.println(kgMap[i][j]);

				}
			}

			for (int i = 0; i <= matrix.mSize * 100 - 1; i++)
			{
				if (kgMap[i][matrix.mSize] != 1)
				{
					// System.out.print("break on :"+ i);
					break;
				}
				System.out.print(i + ": ");
				for (int j = 0; j <= matrix.mSize - 1; j++)
				{

					if (kgMap[i][j] == 1)
						System.out.print(j + " ");

				}
				System.out.println("");

			}
		}

		for (int i = 0; i <= matrix.mSize * 100 - 1; i++)//��������ļ���д��
		{
			graphCount = 0;
			seekNum = i * (matrix.mSize + 1) * Integer.SIZE;
			foutput.seek(seekNum);
			if (kgMap[i][matrix.mSize] != 1)//����ùؼ��ʲ�����
			{
				
				foutput.writeInt(-1);//�ڸ��ļ���ĵ�һ��intд-1
			}
			else
			{
				foutput.seek(seekNum + Integer.SIZE);
				for (int j = 0; j <= matrix.mSize - 1; j++)//��������д����ͼ�ı��
				{

					if (kgMap[i][j] == 1)
					{
						graphCount++;
						foutput.writeInt(j);
					}
				}

			}
			foutput.seek(seekNum);
			foutput.writeInt(graphCount);//�ڸÿ����ǰ��д����Ч����������

		}
		if (debug == 1)
		{
			int tempread;
			for (int i = 0; i <= matrix.mSize * 100 - 1; i++)
			{

				seekNum = i * (matrix.mSize + 1) * Integer.SIZE;
				foutput.seek(seekNum);

				graphCount = foutput.readInt();
				
				if (graphCount == 0)
				{
					
					break;
				}

				else
				{
					foutput.seek(seekNum + Integer.SIZE);
					for (int j = 1; j <= graphCount; j++)
					{

						tempread = foutput.readInt();
						System.out.print(tempread + " ");
					}

				}
				System.out.println(" ");

			}
			System.out.println("-----------------------------------------------------");
		}
		finput.close();
		foutput.close();
	}
}
