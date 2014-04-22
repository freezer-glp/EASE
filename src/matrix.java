import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


class matrix
{
	public static int matrix[][];                    //原始图的邻接矩阵
	public static int poweredMatrix[][];			 //求radius幂后的矩阵
	public static int radius;						//子图的半径
	public static int mSize;						// 矩阵的阶数

	
	static void readMtrix(String filepath, int matrixSize) //从文件中读取原始图的邻接矩阵
	{
		mSize = matrixSize;
		matrix = new int[mSize][mSize];
		Scanner fr = null;
		try
		{
			fr = new Scanner(new File(filepath));
		}
		catch (FileNotFoundException e)
		{
			// TODO 自动生成的 catch 块

			e.printStackTrace();
		}

		for (int i = 0; i <= mSize - 1; i++)
			for (int j = 0; j <= mSize - 1; j++)
			{
				matrix[i][j] = fr.nextInt();
				// System.out.println(matrix[i][j]);
			}
	}

	static void matrixPower(int r)			//进行矩阵的r次求幂
	{
		poweredMatrix = new int[mSize][mSize];
		radius = r;
		int a = 0;
		int temp[][] = new int[mSize][mSize];
		for (int i = 0; i <= mSize - 1; i++)
			for (int j = 0; j <= mSize - 1; j++)
			{
				temp[i][j] = matrix[i][j];
				poweredMatrix[i][j] = matrix[i][j];
			}

		while (r-- != 1)
		{
			for (int i = 0; i <= mSize - 1; i++)
				for (int j = 0; j <= mSize - 1; j++)
				{
					a = 0;
					for (int k = 0; k <= mSize - 1; k++)
						a += temp[i][k] * matrix[k][j];
					poweredMatrix[i][j] = a;
				}

			for (int i = 0; i <= mSize - 1; i++)
				for (int j = 0; j <= mSize - 1; j++)
				{
					temp[i][j] = poweredMatrix[i][j];

				}
		}

		for (int i = 0; i <= mSize - 1; i++)             //将所有非0值用1代替
			for (int j = 0; j <= mSize - 1; j++)
			{
				if (poweredMatrix[i][j] != 0)
					poweredMatrix[i][j] = 1;
			}
	}

	public static void showMatrix(char comm)//显示结果，O为原始邻接矩阵，P为r次幂矩阵
	{
		if (comm == 'O')
		{
			for (int i = 0; i <= mSize - 1; i++)
				for (int j = 0; j <= mSize - 1; j++)
				{
					if (j != mSize - 1)
						System.out.print(matrix[i][j] + " ");
					else
						System.out.println(matrix[i][j]);
				}
		}
		else if (comm == 'P')
		{
			for (int i = 0; i <= mSize - 1; i++)
				for (int j = 0; j <= mSize - 1; j++)
				{
					if (j != mSize - 1)
						System.out.print(poweredMatrix[i][j] + " ");
					else
						System.out.println(poweredMatrix[i][j]);
				}
		}
	}
}
