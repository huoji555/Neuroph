package hwj.demo;

import java.util.Arrays;
import java.util.Scanner;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class feelPos extends NeuralNetwork{
	
	/**
	 * @author Ragty
	 * @param  创建识别坐标系的感知机
	 * @serialData 2018.4.22
	 * @param inputNerouCount
	 * @param outputNeurophCount
	 */
	public void creatNetwork(int inputNerouCount, int outputNeurophCount){
		
		//定义感知机类型
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);
		
		//建立输入神经元，表示输入的刺激
		NeuronProperties inputNeuronProperties = new NeuronProperties();
		inputNeuronProperties.setProperty("neuronType", InputNeuron.class);
		
		//输入神经元构建的输入层
		Layer inputLayer = LayerFactory.createLayer(inputNerouCount, inputNeuronProperties);
		this.addLayer(inputLayer);
		
		//设置传递函数为step函数(即为是大于0为1，小于等于1为0)
		NeuronProperties outputNeuronProperties = new NeuronProperties();
		outputNeuronProperties.setProperty("transferFunction",TransferFunctionType.STEP);
		
		//指定输出层包含两个神经元
		Layer outputLayer = LayerFactory.createLayer(outputNeurophCount, outputNeuronProperties);
		this.addLayer(outputLayer);
		
		//输入输出层全连接
		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);
		
		//设置连接权重(直接关系到神经网络能否正常工作)分 别为[1 0] [0 1]
		//因为要区分四个象限对应为[1,1] [0,1] [0,0] [1,0] stpe函数中负数表示为0
		//根据公式的话，以第一象限为例，要求第一想想上边得出的结果必须为1，下边得出的结果也必须为1
		//据此可推断出权重为 [1 0] [0 1]
		Neuron n = outputLayer.getNeuronAt(0);
		n.getInputConnections()[0].getWeight().setValue(1);   
		n.getInputConnections()[1].getWeight().setValue(0);
		
		n = outputLayer.getNeuronAt(1);
		n.getInputConnections()[0].getWeight().setValue(0);
		n.getInputConnections()[1].getWeight().setValue(1);
		
	}
	
	
	public feelPos (int inputNerouCount, int outputNeurophCount){
		this.creatNetwork(inputNerouCount, outputNeurophCount);
	} 
	
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		String line = null;          
		double[] input = new double[2];
		//两个输入，两个输出的感知机
		feelPos perceptron = new feelPos(2, 2);

		try {
			while ((line = in.nextLine())!= null){
				String[] numbers = line.split("[\\s|,|;]");
				input[0] = Double.parseDouble(numbers[0]);
				input[1] = Double.parseDouble(numbers[1]);
				
				perceptron.setInput(input);
				perceptron.calculate();
				double[] networkOutput = perceptron.getOutput();
				System.out.println(Arrays.toString(input)+ "=" +posToString(networkOutput));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	//判断为第几象限
	public static String posToString(double[] networkOutput ){
		if((networkOutput[0]+networkOutput[1])==2){
			return "第一象限";
		}else if((networkOutput[0]+networkOutput[1])==0){
			return "第三象限";
		}else if((networkOutput[0]-networkOutput[1])==1){
			return "第四象限";
		}
		return "第二象限";
	}
	
	
	
	

}
