package hwj.Learnning;

import java.io.Serializable;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.learning.SupervisedLearning;

//LMS算法
public class LMS extends SupervisedLearning implements Serializable{

	private static final long serialVersionUID = 1L;

	public LMS(){
		
	}
	
	/**
    *@author Ragty
    *@param  LMS核心算法
    *@serialData 2018.4.24
    *@核心公式  deltaWeight = learningRate * neuronError * input(learingRate是学习系数)
    */
	@Override
	protected void updateNetworkWeights(double[] outputError) {
		// TODO Auto-generated method stub
		int i = 0;
		
		//遍历每个神经元，修改权值
		for(Neuron neuron : neuralNetwork.getOutputNeurons()){
			neuron.setError(outputError[i]);
			this.updateNetworkWeights(neuron);
			i++;
		}
		
	}

	
	/**
	 * @author Ragty
	 * @param  迭代更新每个输入神经元的权值
	 * @serialData 2018.4.24
	 * @param neuron
	 */
	protected void updateNetworkWeights(Neuron neuron) {
		// TODO Auto-generated method stub
		//取得神经元误差
		double neuronError = neuron.getError();
		
		//根据所有神经元输入迭代学习
		for(Connection connection : neuron.getInputConnections()){
			//神经元的一个输入
			double input = connection.getInput();
			double weightChange = this.learningRate * neuronError * input;
			
			//更新权值
			Weight weight = connection.getWeight();
			weight.weightChange = weightChange;
			weight.value += weightChange;
		}
		
	}

	
}
