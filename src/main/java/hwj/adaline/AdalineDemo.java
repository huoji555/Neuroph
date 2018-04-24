package hwj.adaline;

import java.util.Arrays;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;

public class AdalineDemo implements LearningEventListener{

	//设置输入神经元的个数为5*7=35个
	public final static int char_width = 5;
	public final static int char_height = 7;
	
	public static String[][] DIGITS = { 
	      { " OOO ",
	        "O   O",
	        "O   O",
	        "O   O",
	        "O   O",
	        "O   O",
	        " OOO "  },

	      { "  O  ",
	        " OO  ",
	        "O O  ",
	        "  O  ",
	        "  O  ",
	        "  O  ",
	        "  O  "  },

	      { " OOO ",
	        "O   O",
	        "    O",
	        "   O ",
	        "  O  ",
	        " O   ",
	        "OOOOO"  },

	      { " OOO ",
	        "O   O",
	        "    O",
	        " OOO ",
	        "    O",
	        "O   O",
	        " OOO "  },

	      { "   O ",
	        "  OO ",
	        " O O ",
	        "O  O ",
	        "OOOOO",
	        "   O ",
	        "   O "  },

	      { "OOOOO",
	        "O    ",
	        "O    ",
	        "OOOO ",
	        "    O",
	        "O   O",
	        " OOO "  },

	      { " OOO ",
	        "O   O",
	        "O    ",
	        "OOOO ",
	        "O   O",
	        "O   O",
	        " OOO "  },

	      { "OOOOO",
	        "    O",
	        "    O",
	        "   O ",
	        "  O  ",
	        " O   ",
	        "O    "  },

	      { " OOO ",
	        "O   O",
	        "O   O",
	        " OOO ",
	        "O   O",
	        "O   O",
	        " OOO "  },

	      { " OOO ",
	        "O   O",
	        "O   O",
	        " OOOO",
	        "    O",
	        "O   O",
	        " OOO "  } };
	
	
	public static void main(String[] args) {
		
		//设置Adaline神经网络输入节点为35个，输出节点为10个
		Adaline ada = new Adaline(char_width * char_height, DIGITS.length);
		
		//设置训练集为35个输入节点，10个输出节点
		DataSet ds = new DataSet(char_width * char_height, DIGITS.length);
		
		//设置训练集(前面是输入值，后面是期望值)
		for(int i = 0; i < DIGITS.length; i++ ){
           ds.addRow(creatTrainRow(DIGITS[i], i));
		}
		//监督训练过程
		ada.getLearningRule().addListener(new AdalineDemo());
		//训练该神经网络
		ada.learn(ds);
		
		//测试训练好的数据
		for(int i = 0; i < DIGITS.length; i++){
			ada.setInput(image2data(DIGITS[i]));
			ada.calculate();
			print(DIGITS[i]);
			System.out.print(maxIndex(ada.getOutput()));
			System.out.println();
		}
		
	}
	
	
	/**
	 * @author Ragty
	 * @param  设置这几个数字的训练集
	 * @serialData 2018.4.24
	 * @param image
	 * @param idealValue
	 * @return
	 */
	public static DataSetRow creatTrainRow(String[] image, int idealValue){
		double[] output = new double[DIGITS.length];
		
		//将训练集初始化
		for(int i = 0; i <DIGITS.length; i++)
			output[i] = -1;
		
		//输入数据
		double[] input = image2data(image);
		
		//用这样的方式来表示一个具体的数字（10个数字分为十个维度，表示哪个数字把哪个数字的维度设置为1）
		output[idealValue] = 1;
		//设置训练集以及期望值
		DataSetRow dsr = new DataSetRow(input, output);
		return dsr;
	}
	
	
	/**
	 * @author Ragty
	 * @param  将输入的二维数组转化为网络能够识别的格式（有字的地方全部转化为1，无字的地方转化为-1）
	 * @serialData 2018.4.24
	 * @param image
	 * @return
	 */
	public static double[] image2data(String[] image){
		double[] input = new double[char_width * char_height];
		
		for(int row = 0; row < char_height; row++){
			for(int col = 0; col < char_width; col++){
				int index = (row*char_width)+col;
				char ch = image[row].charAt(col);
				input[index] = ch == 'O'? 1 :-1;
			}
		}
		
		return input;
	}
	
	
	/**
	 * @author Ragty
	 * @param  识别输出数据为数字（采用竞争规则，在所有维度里，将最大的那个维度视为1，其余均为0）
	 * @param  即找到数组中最大值的索引下标(第一次从左边的条件进入)
	 * @serialData 2018.4.24
	 * @param data
	 * @return
	 */
	public static int maxIndex(double[] data){
		int result = -1;
		for(int i = 0; i < data.length; i++){
			if(result == -1 || data[i] > data[result]){
				result = i;
			}
		}
		return result;
	}
	
	
	/**
	 * @author Ragty
	 * @param  打印输出的打印字体
	 * @serialData 2018.4.24
	 * @param dIGITS2
	 */
	public static void print(String[] dIGITS2){
		
		for(int i = 0; i <dIGITS2.length; i++){
			if(i == dIGITS2.length-1){
				System.out.print(dIGITS2[i]+"===>");
			} else {
				System.out.println(dIGITS2[i]);
			}
		}
		
	}
	
	/**
	 * @param 监督训练
	 */
	@Override
	public void handleLearningEvent(LearningEvent event) {
		// TODO Auto-generated method stub
		IterativeLearning bp = (IterativeLearning)event.getSource();
        System.out.println("iterate:"+bp.getCurrentIteration()); 
        System.out.println(Arrays.toString(bp.getNeuralNetwork().getWeights()));
	}

	
}
