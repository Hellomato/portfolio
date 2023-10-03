function action = qLearningSelectAction( qTable, state )
%Selects action to perform, small chance to choose randomly
    r = rand(1,1);

    if r < 0.2
        action = randi(4,1,1);
    else
        action = qTableMaxAction(qTable, state);
    end

end

