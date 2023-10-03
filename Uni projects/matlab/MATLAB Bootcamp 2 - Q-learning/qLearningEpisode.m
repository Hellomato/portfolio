function [stepsNumof, qTable] = qLearningEpisode(qTable)
    
    ACTIONS_NUMOF = 4;
    GOAL_STATE = 2;

    % Loop until the goal state is reached
    s = rndStartState();
    stepsNumof = 1;
    while s ~= GOAL_STATE
        a = qLearningSelectAction(qTable,s);
        ns = transition(s,a);
        r = reward(s,a);
        qTable = qTableUpdate(qTable,s,a,r,ns);
        stepsNumof = stepsNumof + 1;
        s = ns;
    end
end

