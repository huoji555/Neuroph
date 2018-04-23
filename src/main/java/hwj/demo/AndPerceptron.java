package hwj.demo;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;

//给出学习期望，让感知机学习后 记忆逻辑与
public class AndPerceptron implements LearningEventListener{

	public static void main(String[] args) {
		new AndPerceptron().run();
	}
	
	public void run(){
		
	   //给出学习的训练数据(用于训练神经网络)
	   //数据集有两个输入，一个输出
	   //dataSetRow的构造函数接受两个参数，第一个为输入向量，第二个为期望值
	   DataSet trainningSet = new DataSet(2,1);	
	   trainningSet.addRow(new DataSetRow(new double[]{0,0},new double[]{0}));
	   trainningSet.addRow(new DataSetRow(new double[]{0,1},new double[]{0}));
	   trainningSet.addRow(new DataSetRow(new double[]{1,0},new double[]{0}));
	   trainningSet.addRow(new DataSetRow(new double[]{1,1},new double[]{1}));
	   
	   //创建一个只有两个输入节点的感知机
	   simplePerceptron andPerceptron = new simplePerceptron(2,1);
	   
	   //给学习过程增加事件监听器（监督训练）
	   perceptronLearningRule learningRule = (perceptronLearningRule) andPerceptron.getLearningRule();
	   learningRule.addListener(this);	
	   
	   //使用训练数据训练感知机（进行学习）
	   System.out.println("训练开始");
	   andPerceptron.learn(trainningSet);
	   
	   //测试感知机是否能正确输出
	   System.out.println("测试输出");
	   testNeuralNetwork(andPerceptron, trainningSet);
	}
	
	
	/**
	 * @author Ragty
	 * @param  训练之后对网络测试(测试感知机)
	 * @serialData 2018.4.22
	 * @param neuralNetwork
	 * @param data
	 */
	public static void testNeuralNetwork(NeuralNetwork neuralNetwork, DataSet testSet){
		
		for(DataSetRow testSetRow : testSet.getRows()){
			neuralNetwork.setInput(testSetRow.getInput());
			neuralNetwork.calculate();
			double[] networkOutput = neuralNetwork.getOutput();
			
			System.out.println("Input:"+Arrays.toString(testSetRow.getInput()));
			System.out.println("Output:"+Arrays.toString(networkOutput));
		}
		
	}

	
	//监督训练过程
	@Override
	public void handleLearningEvent(LearningEvent event) {
		// TODO Auto-generated method stub
		//所有迭代学习算法的基类, 它为它的所有子类提供迭代学习过程
		IterativeLearning bp = (IterativeLearning) event.getSource();
		System.out.println("iterate:"+bp.getCurrentIteration());
		System.out.println(Arrays.toString(bp.getNeuralNetwork().getWeights()));
	}
	
	
	
}
