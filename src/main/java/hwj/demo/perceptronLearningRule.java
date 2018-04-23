package hwj.demo;

import java.io.Serializable;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.learning.PerceptronLearning;

public class perceptronLearningRule extends SupervisedLearning implements Serializable{

	private static final long serialVersionUID = 1L;

	public perceptronLearningRule() {

    }
	
	/**
	 * @author Ragty
	 * @param  迭代计算权值
	 * @serialData 2018.4.22
	 */
	@Override
    protected void updateNetworkWeights(double[] outputError) {
        int i = 0;
        for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
            neuron.setError(outputError[i]); 
            double neuronError = neuron.getError();
            // 根据所有的神经元输入 迭代学习
            for (Connection connection : neuron.getInputConnections()) {
                // 神经元的一个输入
                double input = connection.getInput();
                // 计算权值的变更
                double weightChange =  neuronError * input;
                // 更新权值
                Weight weight = connection.getWeight();
                weight.weightChange = weightChange;                
                weight.value += weightChange;
            }

            i++;
        }
    }

	
	

}
