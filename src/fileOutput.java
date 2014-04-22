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
			String mode, int debug) throws IOException // 可以实现按行读取每个顶点包含关键词的信息
	{
		vKeyFilePath = rPath;
		mapFilePath = wPath;
		readmode = mode;
		kgMap = new int[matrix.mSize * 100][matrix.mSize + 1];//2维数组，为1表示该关键词包含在该子图中

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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		int vNum;// 表示当前正在读取的是哪个顶点所包含的关键词，从0号节点开始
		int keyNum, graphNum, graphCount, seekNum;
		
		//从定点--关键词的文件中按行
		for (inString = finput.readLine(), vNum = 0; inString != null; inString = finput
				.readLine(), vNum++)
		{
			temp = inString.split(" "); // 将vNnum节点中的关键词用数组提取出来,temp是String 型数组

			bufferlist = preProcess.vGraphMap.get(vNum);	// 将包含vNum的所有r半径子图存在其中
			if (debug == 1)
			{
				System.out.println("在顶点：" + vNum);
				System.out.println("关键词 size=" + temp.length);
				System.out.println("该顶点对应子图数： " + bufferlist.size());
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
				kgMap[keyNum][matrix.mSize] = 1; // 在数组的最后一列写入1 表示该行有数据

			
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

		for (int i = 0; i <= matrix.mSize * 100 - 1; i++)//按块进行文件的写入
		{
			graphCount = 0;
			seekNum = i * (matrix.mSize + 1) * Integer.SIZE;
			foutput.seek(seekNum);
			if (kgMap[i][matrix.mSize] != 1)//如果该关键词不存在
			{
				
				foutput.writeInt(-1);//在该文件块的第一个int写-1
			}
			else
			{
				foutput.seek(seekNum + Integer.SIZE);
				for (int j = 0; j <= matrix.mSize - 1; j++)//否则依此写入子图的编号
				{

					if (kgMap[i][j] == 1)
					{
						graphCount++;
						foutput.writeInt(j);
					}
				}

			}
			foutput.seek(seekNum);
			foutput.writeInt(graphCount);//在该快的最前面写上有效的数据数量

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
