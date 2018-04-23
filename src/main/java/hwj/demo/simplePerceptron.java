package hwj.demo;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class simplePerceptron extends NeuralNetwork{
	
	private static final long serialVersionUID = 1L;


	public simplePerceptron(int inputNeuralCount,int outputNeuralCount){
		this.creatPerceptron(inputNeuralCount,outputNeuralCount); 
	}
	
	
	/**
	 * @author Ragty
	 * @param  增加学习算法的感知机(记忆逻辑与)
	 * @serialData 2018.4.22
	 * @param inputNeuralCount
	 */
	public void creatPerceptron(int inputNeuralCount, int outputNeuralCount){
		
		//设置类型为感知机
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);
		
		//建立输入神经元，表示输入刺激
		NeuronProperties inputNeuronProperties = new NeuronProperties();
		inputNeuronProperties.setProperty("neuronType",InputNeuron.class);
		
		//建立输入层
		Layer inputLayer = LayerFactory.createLayer(inputNeuralCount, inputNeuronProperties);
		this.addLayer(inputLayer);
		inputLayer.addNeuron(new BiasNeuron());
		
		//建立输出神经元（传输函数为step）
		NeuronProperties outputNeuronProperties = new NeuronProperties();
		outputNeuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		
		//建立输出层
		Layer outputLayer = LayerFactory.createLayer(outputNeuralCount, outputNeuronProperties);
		this.addLayer(outputLayer);
		
		//输入层输出层全连接
		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);
		
		//设置感知机学习算法
		this.setLearningRule(new perceptronLearningRule());
	}
	
	
	

}
