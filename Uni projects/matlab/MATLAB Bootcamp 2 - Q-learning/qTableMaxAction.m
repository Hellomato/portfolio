function action = qTableMaxAction( qTable, state )
%select action with the highest value at a given state
    [value,index] = max(qTable,[],2);

    action = index(state);

end

