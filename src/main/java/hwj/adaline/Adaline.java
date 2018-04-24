package hwj.adaline;

import hwj.Learnning.LMS;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

//自适应线性神经元(采用线性函数Linear)
//学习算法采用LMS算法(根据学习数据，计算总体均方误差，使用最速下降法调整网络的权值和神经元的偏执)
public class Adaline extends NeuralNetwork<LMS>{
	
	private static final long serialVersionUID = 1L;

	public Adaline(int inputNeuralCount,int outputNeuralNetwork) {
		this.creatNetwork(inputNeuralCount, outputNeuralNetwork);
	}

	
	/**
	 * @author Ragty
	 * @param  设置Adaline神经网络
	 * @serialData 2018.4.24
	 * @param inputNeuralCount
	 * @param outputNeuralNetwork
	 */
	public void creatNetwork(int inputNeuralCount, int outputNeuralCount){
		
		//设置神经网络类型为Adaline
		this.setNetworkType(NeuralNetworkType.ADALINE);
		
		//建立输入神经元，表刺激
		NeuronProperties inputNeural = new NeuronProperties();
		inputNeural.setProperty("transferFunction", TransferFunctionType.LINEAR);
	
		//建立神经网络的输入层
		Layer inputLayer = LayerFactory.createLayer(inputNeuralCount, inputNeural);
		inputLayer.addNeuron(new BiasNeuron());
		this.addLayer(inputLayer);
		
		//建立输出神经元
		NeuronProperties outputNeural = new NeuronProperties();
		outputNeural.setProperty("transferFunction", TransferFunctionType.LINEAR);
		
		//创建输出层
		Layer outputLayer = LayerFactory.createLayer(outputNeuralCount, outputNeural);
		this.addLayer(outputLayer);
		
		//输入输出层全连接
		ConnectionFactory.fullConnect(inputLayer, outputLayer);
		NeuralNetworkFactory.setDefaultIO(this);
		
		//设置LMS算法
		//学习步长系数为0.05（由最速下降法引入，表示学习的速度）一般是在0.1或0.01这样的数量级
        //步长太大，不精准，步长太小，学习速度慢，易陷入局部最优
		//设置最大可接受误差为0.5  (LMS中不同于感知机，误差是连续的)
		//(w_new = w_old + 2aep) (b_new = b_old + 2ae)  >>LMS公式（步长系数a，省略常数2）
		LMS lms = new LMS();
		lms.setLearningRate(0.05);
		lms.setMaxError(0.5);
		this.setLearningRule(lms);
		
	}
	
	
	
}
