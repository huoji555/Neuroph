package hwj.demo;

import java.util.Arrays;
import java.util.Random;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;

//感知机经过自动学习，识别象限
public class FeelPosLearning implements LearningEventListener{
	
	public static void main(String[] args) {
		new FeelPosLearning().run();
	}
	
	/**
	 * @author Ragty
	 * @param  根据学习数据自动学习，识别象限
	 */
	public void run(){
		//新建一个两个输入两个输出的训练集
		DataSet dataSet = new DataSet(2,2);
		
		//一万个测试数据，让训练效果更显著
		for (int i = 0; i < 10000; i++){
			//第一象限期望
			dataSet.addRow(new DataSetRow(new double[] { 1 * nextDouble(), 1 * nextDouble() },new double[] { 1, 1 }));
			//第二象限期望
			dataSet.addRow(new DataSetRow(new double[] { -1 * nextDouble(), 1 * nextDouble() },new double[] { 0, 1 }));
			//第三象限期望
			dataSet.addRow(new DataSetRow(new double[] { -1 * nextDouble(), -1 * nextDouble() },new double[] { 0, 0 }));
			//第四象限期望
			dataSet.addRow(new DataSetRow(new double[] { 1 * nextDouble(), -1 * nextDouble() },new double[] { 1, 0 }));
		}
		
		//创建两个输入，两个输出的感知机
		simplePerceptron posPerceptron = new simplePerceptron(2,2);

		//设置最小误差为0.001(增加监听器)
		perceptronLearningRule learningRule = (perceptronLearningRule) posPerceptron.getLearningRule();
		learningRule.setMaxError(0.001);
		learningRule.addListener(this);
		
		//进行学习
		System.out.println("进行学习");
		posPerceptron.learn(dataSet);
		
		//检测学习
		System.out.println("检测学习");
		testData(posPerceptron);
		
	}

	
	/**
	 * @author Ragty
	 * @deprecated 得到一个0到1之间的随机数
	 * @serialData 2018.4.23
	 */
	static Random r = new Random();
	
	public static double nextDouble() {
		double re = 0;
		while ((re = r.nextDouble()) != 0) {
			return re;
		}
		return r.nextDouble();
	}

	
	/**
	 * @author Ragty
	 * @param  测试训练的效果，得出正确率
	 * @param neuralNetwork
	 */
	public static void testData(NeuralNetwork neuralNetwork){
		
		DataSet dataSet = new DataSet(2,2);
		
		for (int i = 0; i < 1000; i++) {
			// 第一象限
			dataSet.addRow(new DataSetRow(new double[] { 1 * nextDouble(), 1 * nextDouble() }, new double[] { 1, 1 }));
			// 第二象限
			dataSet.addRow(new DataSetRow(new double[] { -1 * nextDouble(), 1 * nextDouble() }, new double[] { 0, 1 }));
			// 第三象限
			dataSet.addRow(new DataSetRow(new double[] { -1 * nextDouble(), -1 * nextDouble() }, new double[] { 0, 0 }));
			// 第四象限
			dataSet.addRow(new DataSetRow(new double[] { 1 * nextDouble(), -1 * nextDouble() }, new double[] { 1, 0 }));
		}
		
		//正确总数
		int correctCount = 0;
		int incorrectCount = 0;
		
		//遍历整个测试数组
		for(DataSetRow dataSetRow : dataSet.getRows()){
			//获得一个输入
			neuralNetwork.setInput(dataSetRow.getInput());
			neuralNetwork.calculate();
			double[] output = neuralNetwork.getOutput();
			
			//实际输出跟期望输出相比较
			if(Arrays.equals(output, dataSetRow.getDesiredOutput())){
				correctCount++;
			} else{
				incorrectCount++;
			}
			
		}
		System.out.println("正确率："+correctCount * 1.0 / (correctCount + incorrectCount));
		
	}
	
	
	/**
	 * @author Ragty
	 * @param  监督学习
	 * @serialData 2018.4.23
	 */
	@Override
	public void handleLearningEvent(LearningEvent event) {
		// TODO Auto-generated method stub
		IterativeLearning bp = (IterativeLearning) event.getSource();
		System.out.println("iterate:" + bp.getCurrentIteration());
		System.out.print("TotalNetworkError:");
		System.out.println(((perceptronLearningRule) bp.getNeuralNetwork().getLearningRule()).getTotalNetworkError());
		
	}

}
