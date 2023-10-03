function newqTable = qTableUpdate( qTable, state, action, reward, nextState )
%Update the Qtable according to the Algorithm given
%   Detailed explanation goes here
    
    maxValue = max(qTable(nextState,:));
    
    currentValue = qTable(state,action);
    
    newqTable = qTable;
    
    newqTable(state,action) = currentValue + 0.2 *(reward + 0.9 * maxValue - currentValue);
    

end

