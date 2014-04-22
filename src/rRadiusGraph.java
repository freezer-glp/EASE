
class rRadiusGraph
{
	static int num = 0;				//r半径子图计数器
	double score;					//子图的得分
	int r;							//子图的半径
	int id;							//子图的编号		
	int M[][];						//子图的邻接矩阵

	public rRadiusGraph(int R, int Id, int m[][])
	{
		this.M = new int[matrix.mSize][matrix.mSize];
		rRadiusGraph.num++;
		this.r = R;
		this.id = Id;
		for (int i = 0; i <= matrix.mSize - 1; i++)
			for (int j = 0; j <= matrix.mSize - 1; j++)
				this.M[i][j] = m[i][j];

		

	}
}
