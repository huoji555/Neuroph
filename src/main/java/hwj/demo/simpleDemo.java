package hwj.demo;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class simpleDemo extends NeuralNetwork{
	
	public simpleDemo(int inputCount){
		this.creatNetwork(inputCount);
	}
	
	
	/**
	 * @author Ragty
	 * @param  新建感知机
	 * @serialData  2018.4.20
	 * @param inputNeuronsCount
	 */
	private void creatNetwork(int inputNeuronsCount){
		
		//设置网络类别为感知机
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);
		
		
		//建立输入神经元，表示输入的刺激
		NeuronProperties inputNeuron = new NeuronProperties();
		inputNeuron.setProperty("neuronType", InputNeuron.class);
		
		
		//由输入神经元构成的底层
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuron);
		this.addLayer(inputLayer);
		//在输入层增加贝叶斯神经，表示神经元偏置
		inputLayer.addNeuron(new BiasNeuron());
		
		
		//设置传递函数为step()函数
		NeuronProperties outputProperties = new NeuronProperties();
		outputProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		Layer outputLayer = LayerFactory.createLayer(1, outputProperties);
		this.addLayer(outputLayer);
		
		
		//将输入层和输出层进行全连接(输入节点和每个神经元都两两连接)
		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);
		Neuron n = outputLayer.getNeuronAt(0);
		
		System.out.println(n);
		
		//设置每个连接的权重，1和1是输入节点到神经元的权值，-1.5是神经元的偏置
		n.getInputConnections()[0].getWeight().setValue(1);
		n.getInputConnections()[1].getWeight().setValue(1);
		n.getInputConnections()[2].getWeight().setValue(-1.5);
		
	}
	
    //使用感知机记忆逻辑与
	public static void main(String[] args) {
		
		//创建学习数据集
		DataSet trainingSet = new DataSet(2,1);  //两个输入，一个输出
		trainingSet.addRow(new DataSetRow(new double[] {0,0},new double[] {Double.NaN}));
		trainingSet.addRow(new DataSetRow(new double[] {0,1},new double[] {Double.NaN}));
		trainingSet.addRow(new DataSetRow(new double[] {1,0},new double[] {Double.NaN}));
		trainingSet.addRow(new DataSetRow(new double[] {1,1},new double[] {Double.NaN}));
		
		simpleDemo perceptron = new simpleDemo(2);
		
		for(DataSetRow row : trainingSet.getRows()){
			perceptron.setInput(row.getInput());
			perceptron.calculate();
			double[] networkOutput = perceptron.getOutput();
			System.out.println(Arrays.toString(row.getInput())+"="+Arrays.toString(networkOutput));
		}
		
		
	}
	
	
}
